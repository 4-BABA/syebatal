package io.baba4.syebatal.core.elm

import io.baba4.syebatal.Battlefield1
import io.baba4.syebatal.Player


data class State(
    val currentPlayerIndex: Int,
    val players: List<Player>,
    val battlefields: List<Battlefield1>
) : Elm.State, Elm.ViewData
