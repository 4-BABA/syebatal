package io.baba4.syebatal

import io.baba4.syebatal.Orientation.*

data class Ship(
    val points: List<Point>
) {
    constructor(vararg points: Point) : this(points.toList())

    val orientation: Orientation = when {
        points.distinctBy { it.row }.size == 1 -> HORIZONTAL
        points.distinctBy { it.column }.size == 1 -> VERTICAL
        else -> throw error("Ship should be one of ${Orientation.values()}")
    }
}

fun Ship.pointsAround(bfSize: Int): Set<Point> =
    points.map { it.pointsAround(bfSize) }.flatten().toSet() - points
