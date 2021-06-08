package io.baba4.syebatal.core.elm

import io.baba4.syebatal.Battlefield
import io.baba4.syebatal.Player


data class State(
    val currentPlayerIndex: Int,
    val players: List<Player>,
    val battlefields: List<Battlefield>
) : Elm.State, Elm.ViewData
