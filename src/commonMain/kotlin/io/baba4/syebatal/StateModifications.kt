package io.baba4.syebatal

import io.baba4.syebatal.elm.State
import io.baba4.syebatal.models.Player
import io.baba4.syebatal.models.Point


val State.currentPlayer: Player get() = players[currentPlayerIndex]
val State.nextPlayerIndex: Int get() = (currentPlayerIndex + 1) % players.size

fun State.shoot(point: Point): State = copy(
    currentPlayerIndex = nextPlayerIndex,
    battlefields = battlefields.mapIndexed { index, battlefield ->
        if (index == currentPlayerIndex) battlefield
        else battlefield.shotAt(point)
    }
)
