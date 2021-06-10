package io.baba4.syebatal

import io.baba4.syebatal.core.elm.State


val State.currentPlayer: Player get() = players[currentPlayerIndex]

fun State.shoot(point: Point): State = copy(
    battlefields = battlefields.mapIndexed { index, battlefield ->
        if (index == currentPlayerIndex) battlefield
        else battlefield.shotAt(point)
    }
)
