package io.baba4.syebatal.core.elm


class StateMachine(
    initialState: State,
    effectHandler: EffectHandler = EffectHandler()
) : Elm.Controller<State, Action, Effect> by Elm.ControllerImpl(
    initialState = initialState,
    reducer = Reducer(),
    effectHandler = effectHandler,
    converterFactory = ConverterFactory()
)
