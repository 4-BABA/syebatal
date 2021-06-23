package io.baba4.syebatal.models

import kotlin.random.Random


fun generate(size: Int): Battlefield1 {
    val bf = Battlefield1(size) { BfCell.UNCHECKED }
//    bf.addFirstShip(size) todo: decide
    for (shipSize in Battlefield1.MAX_SHIP_SIZE downTo 1) {
        val shipsAmount = Battlefield1.MAX_SHIP_SIZE - shipSize + 1
        repeat(shipsAmount) {
            val (start1, end1) = bf.findSpace(shipSize)
            bf.addShip(start1, end1)
        }
    }
    // todo: fill singletons especially
    bf.table.replace(BfCell.EMPTY, BfCell.UNCHECKED)
    return bf
}

private fun Battlefield1.addFirstShip(size: Int) {
    val orientation = Orientation.values().random()
    val (start, end) = randomStartPoints(orientation, size)
    val firstShip = Ship1(pointsRange(start, end))
    addShip(firstShip)
}

internal fun Battlefield1.findSpace(shipSize: Int): Pair<Point, Point> {
    val range = uncheckedRanges(Orientation.values().random())
        .filter { distance(it.first, it.second) >= shipSize }
        .random()
    return narrowRandomly(range, shipSize)
}

private fun narrowRandomly(range: Pair<Point, Point>, shipSize: Int): Pair<Point, Point> {
    val (start, end) = range
    val gap = distance(start, end) - shipSize
    if (gap == 0)
        return range
    val shift = Random.nextInt(gap)
    return if (start.row == end.row) {
        val newStart = Point(start.row, start.column + shift)
        val newEnd = Point(newStart.row, newStart.column + shipSize - 1)
        Pair(newStart, newEnd)
    } else {
        val newStart = Point(start.row + shift, start.column)
        val newEnd = Point(newStart.row + shipSize - 1, newStart.column)
        Pair(newStart, newEnd)
    }
}

internal fun Battlefield1.uncheckedRanges(orientation: Orientation): List<Pair<Point, Point>> {
    val ranges = mutableListOf<Pair<Point, Point>>()
    val append: (MutableList<Point>) -> Unit = { pretender ->
        if (pretender.isNotEmpty())
            ranges.add(Pair(pretender.first(), pretender.last()))
    }
    val accessTable: (Int, Int) -> BfCell = when (orientation) {
        Orientation.HORIZONTAL -> { row, col -> table[row][col] }
        Orientation.VERTICAL -> { col, row -> table[row][col] }
    }
    val createPoint: (Int, Int) -> Point = when (orientation) {
        Orientation.HORIZONTAL -> { row, col -> Point(row, col) }
        Orientation.VERTICAL -> { col, row -> Point(row, col) }
    }
    for (dim1 in 0 until size) {
        var pretender = mutableListOf<Point>()
        for (dim2 in 0 until size) {
            if (BfCell.UNCHECKED == accessTable(dim1, dim2)) {
                pretender.add(createPoint(dim1, dim2))
            } else {
                append(pretender)
                pretender = mutableListOf()
            }
        }
        append(pretender)
    }
    return ranges
}

internal fun randomStartPoints(orientation: Orientation, size: Int) = when (orientation) {
    Orientation.HORIZONTAL -> {
        val start = Point(Random.nextInt(size), Random.nextInt(size - Battlefield1.MAX_SHIP_SIZE))
        val end = Point(start.row, start.column + Battlefield1.MAX_SHIP_SIZE - 1)
        start to end
    }
    Orientation.VERTICAL -> {
        val start = Point(Random.nextInt(size - Battlefield1.MAX_SHIP_SIZE), Random.nextInt(size))
        val end = Point(start.row + Battlefield1.MAX_SHIP_SIZE - 1, start.column)
        start to end
    }
}

private fun Array<Array<BfCell>>.replace(cell1: BfCell, cell2: BfCell) {
    (0 until size).forEach { row ->
        (0 until size)
            .filter { this[row][it] == cell1 }
            .forEach { this[row][it] = cell2 }
    }
}