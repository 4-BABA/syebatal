package io.baba4.syebatal

data class Ship(
    val orientation: Orientation,
    val points: List<Point>
)

fun Ship.pointsAround(bfSize: Int): Set<Point> =
    points.map { it.pointsAround(bfSize) }.flatten().toSet() - points
