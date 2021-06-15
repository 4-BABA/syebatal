package io.baba4.syebatal.models


data class Point(val row: Int, val column: Int)


fun Point.isValid(bfSize: Int): Boolean =
    row in 0 until bfSize && column in 0 until bfSize

fun Point.takeIfIsValid(bfSize: Int): Point? =
    takeIf { it.isValid(bfSize) }

fun Point.left(bfSize: Int): Point? = copy(column = column - 1).takeIfIsValid(bfSize)
fun Point.top(bfSize: Int): Point? = copy(row = row - 1).takeIfIsValid(bfSize)
fun Point.right(bfSize: Int): Point? = copy(column = column + 1).takeIfIsValid(bfSize)
fun Point.bottom(bfSize: Int): Point? = copy(row = row + 1).takeIfIsValid(bfSize)

fun Point.topLeft(bfSize: Int): Point? = top(bfSize)?.left(bfSize)
fun Point.topRight(bfSize: Int): Point? = top(bfSize)?.right(bfSize)
fun Point.bottomLeft(bfSize: Int): Point? = bottom(bfSize)?.left(bfSize)
fun Point.bottomRight(bfSize: Int): Point? = bottom(bfSize)?.right(bfSize)

fun Point.pointsAround(bfSize: Int): Set<Point> = setOfNotNull(
    left(bfSize), top(bfSize), right(bfSize), bottom(bfSize),
    topLeft(bfSize), topRight(bfSize), bottomLeft(bfSize), bottomRight(bfSize)
)