package io.baba4.syebatal.models

import io.baba4.syebatal.models.Orientation.*


data class Ship1(
    val points: List<Point>
) {
    constructor(vararg points: Point) : this(points.toList())

    val orientation: Orientation = when {
        points.distinctBy { it.row }.size == 1 -> HORIZONTAL
        points.distinctBy { it.column }.size == 1 -> VERTICAL
        else -> throw error("Ship should be one of ${Orientation.values()}")
    }
}

fun Ship1.pointsAround(bfSize: Int): Set<Point> =
    points.map { it.pointsAround(bfSize) }.flatten().toSet() - points
