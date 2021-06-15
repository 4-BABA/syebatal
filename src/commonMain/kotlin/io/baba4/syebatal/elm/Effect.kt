package io.baba4.syebatal.elm


sealed class Effect : Elm.Effect {
    data class IllegalTransition(val state: State, val action: Action) : Effect()
}
