package io.baba4.syebatal.elm

import com.baba4.syebatal.models.Action.Preparation
import com.baba4.syebatal.models.PlayerId
import com.baba4.syebatal.models.ViewData

class PreparationStage(
    val roomId: StageId = generateStageId(),
    private val roomSize: Int = 4,
    private val closeListener: StageCloseListener,
) : Stage {
    data class State(
        val readiness: Map<PlayerId, Boolean> = emptyMap(),
    ) : Elm.State

    sealed class Effect : Elm.Effect {
        object HandleEmptyRoom : Effect()
        object HandleReadyRoom : Effect()
    }


    private val reducer = Elm.Reducer<State, Preparation, Effect> { state, action, playerId ->
        when (action) {
            Preparation.Join -> handleJoinAction(state, playerId)
            is Preparation.Ready -> handleReadyAction(state, action, playerId)
            Preparation.Leave -> handleLeaveAction(state, playerId)
        }
    }

    private val effectHandler = Elm.EffectHandler<Effect, Preparation> {  effect ->
        when (effect) {
            Effect.HandleEmptyRoom -> closeListener.closeOnFailure(roomId)
            Effect.HandleReadyRoom -> closeListener.closeOnSuccess(roomId)
        }
        return@EffectHandler null
    }

    fun viewDataConverter(playerStorage: PlayerStorage) =
        Elm.ViewDataConverter<State, ViewData.Preparation> { state ->
            ViewData.Preparation(
                readiness = state.readiness.mapKeys { (playerId, _) -> playerStorage.getPlayer(playerId) }
            )
        }


    override val store = Elm.Store.create(
        initialState = State(),
        reducer = reducer,
        effectHandler = effectHandler,
    )

    val isFull: Boolean get() = store.currentState.readiness.size == roomSize
    val isNotFull: Boolean get() = !isFull


    private fun handleJoinAction(state: State, playerId: PlayerId): Pair<State, Effect?> {
        if (state.readiness.size == roomSize) return state to null

        val newReadiness = state.readiness.toMutableMap()
        newReadiness[playerId] = false

        return state.copy(readiness = newReadiness) to null
    }

    private fun handleReadyAction(
        state: State,
        action: Preparation.Ready,
        playerId: PlayerId,
    ): Pair<State, Effect?> {
        val newReadiness = state.readiness.toMutableMap()
        newReadiness[playerId] = action.ready

        val newState = state.copy(readiness = newReadiness)
        val effect = Effect.HandleReadyRoom
            .takeIf { newReadiness.size == roomSize && newReadiness.allValuesAreTrue() }

        return newState to effect
    }

    private fun handleLeaveAction(state: State, playerId: PlayerId): Pair<State, Effect?> {
        val newReadiness = state.readiness.toMutableMap()
        newReadiness.remove(playerId)

        val newState = state.copy(readiness = newReadiness)
        val effect = Effect.HandleEmptyRoom
            .takeIf { newReadiness.isEmpty() }

        return newState to effect
    }


    private fun <T> Map<T, Boolean>.allValuesAreTrue() = values.all { it }
}
