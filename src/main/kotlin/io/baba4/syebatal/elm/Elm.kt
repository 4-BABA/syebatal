package io.baba4.syebatal.elm

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


interface Elm {
    interface State
    interface Action
    interface Effect
    interface ViewData

    fun interface Reducer<S : State, A : Action, E : Effect> {
        fun reduce(state: S, action: A): Pair<S, E?>
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

        fun start()
        fun stop()

        fun setAction(action: A)

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
        private val actionFlow = MutableSharedFlow<A>()
        private var actionScope: CoroutineScope = createNewActionScope()

        override fun start() {
            actionScope.launch {
                actionFlow.collect { newAction ->
                    ensureActive()
                    onActionReceived(newAction)
                }
            }
        }

        override fun stop() {
            actionScope.cancel()
            actionScope = createNewActionScope()
        }

        override fun setAction(action: A) {
            actionScope.launch { actionFlow.emit(action) }
        }

        private fun createNewActionScope() = CoroutineScope(Dispatchers.Default)


        override val stateFlow = MutableStateFlow(initialState)

        override fun <VD : ViewData> viewDataFlow(converter: ViewDataConverter<S, VD>): Flow<VD> =
            stateFlow.map(converter::convert)


        private suspend fun onActionReceived(action: A) {
            val (newState, effect) = reducer.reduce(currentState, action)

            if (newState != currentState) {
                stateFlow.emit(newState)
            }

            if (effect != null) actionScope.launch {
                val newAction = effectHandler.handle(effect)
                if (newAction != null) actionFlow.emit(newAction)
            }
        }
    }
}
