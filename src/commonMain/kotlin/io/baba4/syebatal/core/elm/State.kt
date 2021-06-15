package io.baba4.syebatal.core.elm

import io.baba4.syebatal.Battlefield1
import io.baba4.syebatal.Player


data class State(
    val currentPlayerIndex: Int,
    val players: List<Player>,
    val battlefields: List<Battlefield1>
) : Elm.State, Elm.ViewData {
    init {
        require(currentPlayerIndex < players.size) { "\'currentPlayerIndex\' should be in [0 .. players.size]" }
        require(players.size == battlefields.size) { "The number of players and battlefields should be the same" }
    }
}
