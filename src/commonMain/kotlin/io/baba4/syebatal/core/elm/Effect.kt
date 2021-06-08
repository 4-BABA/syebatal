package io.baba4.syebatal.core.elm


sealed class Effect : Elm.Effect {
    data class IllegalTransition(val state: State, val action: Action) : Effect()
}
