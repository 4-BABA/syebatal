package io.baba4.syebatal.core.elm

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.reflect.KClass


interface Elm {
    interface State
    interface Action
    interface Effect
    interface ViewData

    interface Reducer<S : State, A : Action, E : Effect> {
        fun reduce(state: S, action: A): Pair<S, E?> =
            if (!allowedCondition(state, action)) state to illegalStateEffect(state, action)
            else reduceValid(state, action)
        fun allowedCondition(state: S, action: A): Boolean = true
        fun illegalStateEffect(state: S, action: A): E
        fun reduceValid(state: S, action: A): Pair<S, E?>
    }

    interface EffectHandler<E : Effect, A : Action> {
        suspend fun handle(effect: E): A?
    }

    interface ViewDataConverter<S : State, VD : ViewData> {
        fun convert(state: S): VD
    }

    interface ViewDataConvertersFactory<S : State> {
        fun <VD : ViewData> get(kClass: KClass<VD>): ViewDataConverter<S, VD>
    }

    interface Controller<S : State, A : Action, E : Effect> {
        val effectHandler: EffectHandler<E, A>
        val reducer: Reducer<S, A, E>
        val converterFactory: ViewDataConvertersFactory<S>

        fun start()
        fun stop()

        fun setAction(action: A)

        val stateFlow: StateFlow<S>
        val currentState: S get() = stateFlow.value
        fun <VD : ViewData> viewDataFlow(vdClass: KClass<VD>): Flow<VD>
    }


    class ControllerImpl<S : State, A : Action, E : Effect>(
        initialState: S,
        override val effectHandler: EffectHandler<E, A>,
        override val reducer: Reducer<S, A, E>,
        override val converterFactory: ViewDataConvertersFactory<S>
    ) : Controller<S, A, E> {
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

        private fun createNewActionScope() = CoroutineScope(Dispatchers.Default)

        override fun setAction(action: A) {
            actionScope.launch { actionFlow.emit(action) }
        }


        override val stateFlow = MutableStateFlow(initialState)

        override fun <VD : ViewData> viewDataFlow(vdClass: KClass<VD>): Flow<VD> {
            val converter = converterFactory.get(vdClass)
            return stateFlow.map(converter::convert)
        }


        private suspend fun onActionReceived(action: A) {
            val (newState, effect) = reducer.reduce(currentState, action)
            if (newState != currentState) {
                stateFlow.value = newState
            }
            if (effect != null) actionScope.launch {
                val newAction = effectHandler.handle(effect)
                if (newAction != null) actionFlow.emit(newAction)
            }
        }
    }
}
