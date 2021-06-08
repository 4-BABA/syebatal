package io.baba4.syebatal.core.elm


class EffectHandler : Elm.EffectHandler<Effect, Action> {
    override suspend fun handle(effect: Effect): Action? = when (effect) {
        is Effect.IllegalTransition -> null
    }
}
