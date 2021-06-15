package io.baba4.syebatal.elm

import io.baba4.syebatal.shoot


class Reducer : Elm.Reducer<State, Action, Effect> {
    override fun illegalStateEffect(state: State, action: Action): Effect = Effect.IllegalTransition(state, action)

    override fun reduceValid(state: State, action: Action): Pair<State, Effect?> = when (action) {
        is Action.Shoot -> state.shoot(action.point) to null
    }
}
