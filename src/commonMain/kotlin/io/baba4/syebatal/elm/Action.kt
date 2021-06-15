package io.baba4.syebatal.elm

import io.baba4.syebatal.models.Point


sealed class Action : Elm.Action {
    data class Shoot(val point: Point) : Action()
}
