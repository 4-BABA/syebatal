package io.baba4.syebatal.models

import io.baba4.syebatal.models.BfCell.*


data class Ship2(
    val decks: Map<Point, BfCell>
) {
    init {
        require(cells.all { it.isShip }) {
            "${Ship2::class.simpleName} can't hold non ship values ${BfCell.values().filter { it.isNotShip }}"
        }
    }
    constructor(vararg points: Pair<Point, BfCell>) : this(points.toMap())

    val points: Set<Point> get() = decks.map { it.key }.toSet()
    val cells: Set<BfCell> get() = decks.map { it.value }.toSet()

    val orientation: Orientation = when {
        points.distinctBy { it.row }.size == 1 -> Orientation.HORIZONTAL
        points.distinctBy { it.column }.size == 1 -> Orientation.VERTICAL
        else -> throw error("Ship should be one of ${Orientation.values()}")
    }
}

fun Ship2.pointsAround(bfSize: Int): Set<Point> =
    points.map { it.pointsAround(bfSize) }.flatten().toSet() - points

val Ship2.isSunk: Boolean get() = cells.all { it == DAMAGED }

fun Collection<Ship2>.toUnifiedDecksMap(): Map<Point, BfCell> =
    this.map { ship ->
        ship.decks.map { entry -> entry.toPair() }
    }.flatten().toMap()
