package io.baba4.syebatal.models

import io.baba4.syebatal.models.Battlefield1.Companion.MAX_SHIP_SIZE
import io.baba4.syebatal.models.BfCell.*
import io.baba4.syebatal.models.Orientation.*
import kotlin.math.sqrt
import kotlin.random.Random


class Battlefield1(val size: Int, initial: (Point) -> BfCell) {
    init {
        require(size >= 0) { "Can't create ${Battlefield1::class.simpleName} with negative size [$size]." }
    }
    val table: Array<Array<BfCell>> = Array(size) { row -> Array(size) { column -> initial(Point(row, column)) } }

    override fun toString(): String = "$size$SEPARATOR\n" + buildString {
        (0 until size).forEach { row ->
            (0 until size).forEach { column ->
                append(table[row][column].symbol)
            }
            append("\n")
        }
    }

    operator fun Array<Array<BfCell>>.get(point: Point): BfCell = this[point.row][point.column]
    operator fun Array<Array<BfCell>>.set(point: Point, cell: BfCell) {
        this[point.row][point.column] = cell
    }

    override fun equals(other: Any?): Boolean =
        other is Battlefield1 && this.size == other.size && this.table.contentDeepEquals(other.table)

    override fun hashCode(): Int =
        this.size.hashCode() + 31 * table.contentDeepHashCode()


    fun encode() = this.table.joinToString(separator = "", prefix = "${this.size}$SEPARATOR") { row ->
        row.joinToString(separator = "") { it.symbol.toString() }
    }

    fun isSunk(ship: Ship1): Boolean = ship.points.all { table[it] == DAMAGED }

    fun shotAt(point: Point): Battlefield1 {
        val newValue = when (val oldValue = table[point]) {
            UNCHECKED -> EMPTY
            FILLED -> DAMAGED
            else -> oldValue
        }

        return Battlefield1(size) { (row, column) ->
            val currentPoint = Point(row, column)
            if (point == currentPoint) newValue else table[currentPoint]
        }.let { battlefield ->
            Battlefield1(size) { (row, column) ->
                val currentPoint = Point(row, column)
                val ship = battlefield.findShip(point)
                val aroundPointForSunkShip = newValue == DAMAGED &&
                        ship != null &&
                        battlefield.isSunk(ship) &&
                        currentPoint in ship.pointsAround(size)
                when {
                    aroundPointForSunkShip -> EMPTY
                    else -> battlefield.table[currentPoint]
                }
            }
        }
    }

    private fun findShip(point: Point): Ship1? {
        if (table[point].isNotShip) return null

        val leftPoint = point.left(size)
        val topPoint = point.top(size)
        val rightPoint = point.right(size)
        val bottomPoint = point.bottom(size)
        val orientation = when {
            leftPoint != null && table[leftPoint].isShip || rightPoint != null && table[rightPoint].isShip -> HORIZONTAL
            topPoint != null && table[topPoint].isShip || bottomPoint != null && table[bottomPoint].isShip -> VERTICAL
            else -> return Ship1(point)
        }
        val points = when (orientation) {
            HORIZONTAL -> takeHorizontalPointsWhileShip(point.row, range = point.column - 1 downTo 0).asReversed() +
                    point +
                    takeHorizontalPointsWhileShip(point.row, range = point.column + 1 until size)
            VERTICAL -> takeVerticalPointsWhileShip(point.column, range = point.row - 1 downTo 0).asReversed() +
                    point +
                    takeVerticalPointsWhileShip(point.column, range = point.row + 1 until size)
        }
        return Ship1(points)
    }

    private fun takeHorizontalPointsWhileShip(row: Int, range: IntProgression): List<Point> =
        range.map { Point(row, column = it) }.takeWhile { table[it].isShip }

    private fun takeVerticalPointsWhileShip(column: Int, range: IntProgression): List<Point> =
        range.map { Point(row = it, column) }.takeWhile { table[it].isShip }

    fun addShip(point1: Point, point2: Point = point1) {
        addShip(Ship1(point1, point2))
    }

    fun addShip(ship: Ship1) {
        ship.points.forEach { table[it] = FILLED }
        ship.pointsAround(size).forEach { table[it] = EMPTY }
    }

    companion object {
        private const val SEPARATOR = '|'
        const val MAX_SHIP_SIZE = 3

        fun decode(string: String): Battlefield1 {
            require(string.length >= 0) {
                "Can't decode ${Battlefield1::class.simpleName} with negative length [${string.length}]."
            }

            val (sizeString, content) = string.split(SEPARATOR, limit = 2)
            val size = sizeString.toInt()
            val sizeByContent = sqrt(content.length.toDouble()).toInt()
            require(sizeByContent == size) {
                "Invalid decoded matrix. Size should be $size, but actual $sizeByContent."
            }

            return Battlefield1(size) { (row, column) -> BfCell.fromSymbol(content[row * size + column]) }
        }
    }
}

fun generate(size: Int): Battlefield1 {
    val bf = Battlefield1(size) { UNCHECKED }
    bf.addFirstShip(size)
    for (shipSize in MAX_SHIP_SIZE - 1 downTo 1) {
        repeat(MAX_SHIP_SIZE - shipSize + 1) {
            val (start1, end1) = bf.findSpace(shipSize)
            bf.addShip(start1, end1)
        }
    }
    // todo: fill singletons especially
    // todo: replace empties with unchecked (or vise versa)
    return bf
}

private fun Battlefield1.addFirstShip(size: Int) {
    val orientation = Orientation.values().random()
    val (start, end) = randomStartPoints(orientation, size)
    val firstShip = Ship1(pointsRange(start, end))
    addShip(firstShip)
}

internal fun Battlefield1.findSpace(shipSize: Int): Pair<Point, Point> {
    val rawRanges = when (Orientation.values().random()) {
        HORIZONTAL -> uncheckedHorizontalRanges()
        VERTICAL -> uncheckedVerticalRanges()
    }
    val range = rawRanges
        .filter { distance(it.first, it.second) >= shipSize }
        .random()
    return narrow(range, shipSize)
}

private fun narrow(range: Pair<Point, Point>, shipSize: Int): Pair<Point, Point> {
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

internal fun Battlefield1.uncheckedHorizontalRanges(): List<Pair<Point, Point>> {
    val ranges = mutableListOf<Pair<Point, Point>>()
    val append: (MutableList<Point>) -> Unit = { pretender ->
        if (pretender.isNotEmpty())
            ranges.add(Pair(pretender.first(), pretender.last()))
    }
    for (row in 0 until size) {
        var pretender = mutableListOf<Point>()
        for (col in 0 until size) {
            if (UNCHECKED == table[row][col]) {
                pretender.add(Point(row, col))
            } else {
                append(pretender)
                pretender = mutableListOf()
            }
        }
        append(pretender)
    }
    return ranges
}

internal fun Battlefield1.uncheckedVerticalRanges(): List<Pair<Point, Point>> {
    val ranges = mutableListOf<Pair<Point, Point>>()
    val append: (MutableList<Point>) -> Unit = { pretender ->
        if (pretender.isNotEmpty())
            ranges.add(Pair(pretender.first(), pretender.last()))
    }
    // todo: refactor
    for (col in 0 until size) {
        var pretender = mutableListOf<Point>()
        for (row in 0 until size) {
            if (UNCHECKED == table[row][col]) {
                pretender.add(Point(row, col))
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
    HORIZONTAL -> {
        val start = Point(Random.nextInt(size), Random.nextInt(size - MAX_SHIP_SIZE))
        val end = Point(start.row, start.column + MAX_SHIP_SIZE - 1)
        start to end
    }
    VERTICAL -> {
        val start = Point(Random.nextInt(size - MAX_SHIP_SIZE), Random.nextInt(size))
        val end = Point(start.row + MAX_SHIP_SIZE - 1, start.column)
        start to end
    }
}
