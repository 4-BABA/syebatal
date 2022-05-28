package io.baba4.syebatal.elm

import com.baba4.syebatal.models.Action
import com.baba4.syebatal.models.PlayerId
import com.baba4.syebatal.models.ViewData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


interface Elm {
    interface State
    interface Effect

    fun interface Reducer<S : State, A : Action, E : Effect> {
        fun reduce(state: S, action: A, playerId: PlayerId): Pair<S, E?>
    }

    fun interface EffectHandler<E : Effect, A : Action> {
        suspend fun handle(effect: E): A?
    }

    fun interface ViewDataConverter<S : State, VD : ViewData> {
        fun convert(state: S): VD
    }

    interface Store<S : State, A : Action, E : Effect> {
        val reducer: Reducer<S, A, E>
        val effectHandler: EffectHandler<E, A>

        fun attach()
        fun detach()

        fun setAction(action: A, playerId: PlayerId)

        val stateFlow: StateFlow<S>
        val currentState: S get() = stateFlow.value
        fun <VD : ViewData> viewDataFlow(converter: ViewDataConverter<S, VD>): Flow<VD>

        companion object {
            fun <S : State, A : Action, E : Effect> create(
                initialState: S,
                reducer: Reducer<S, A, E>,
                effectHandler: EffectHandler<E, A>,
            ): Store<S, A, E> = StoreImpl(initialState, reducer, effectHandler)
        }
    }


    private class StoreImpl<S : State, A : Action, E : Effect>(
        initialState: S,
        override val reducer: Reducer<S, A, E>,
        override val effectHandler: EffectHandler<E, A>,
    ) : Store<S, A, E> {
        private val actionFlow = MutableSharedFlow<Pair<A, PlayerId>>()
        private var actionScope: CoroutineScope = createNewActionScope()

        override fun attach() {
            actionScope.launch {
                actionFlow.collect { (newAction, playerId) ->
                    ensureActive()
                    onActionReceived(newAction, playerId)
                }
            }
        }

        override fun detach() {
            actionScope.cancel()
            actionScope = createNewActionScope()
        }

        override fun setAction(action: A, playerId: PlayerId) {
            actionScope.launch { actionFlow.emit(action to playerId) }
        }

        private fun createNewActionScope() = CoroutineScope(Dispatchers.Default)


        override val stateFlow = MutableStateFlow(initialState)

        override fun <VD : ViewData> viewDataFlow(converter: ViewDataConverter<S, VD>): Flow<VD> =
            stateFlow.map(converter::convert)


        private suspend fun onActionReceived(action: A, playerId: PlayerId) {
            val (newState, effect) = reducer.reduce(currentState, action, playerId)

            if (newState != currentState) {
                stateFlow.emit(newState)
            }

            if (effect != null) actionScope.launch {
                val newAction = effectHandler.handle(effect)
                if (newAction != null) actionFlow.emit(newAction to playerId)
            }
        }
    }
}
