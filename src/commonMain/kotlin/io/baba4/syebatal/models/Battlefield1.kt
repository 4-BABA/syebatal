package io.baba4.syebatal.models

import io.baba4.syebatal.models.BfCell.*
import io.baba4.syebatal.models.Orientation.*
import kotlin.math.sqrt


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


    companion object {
        private const val SEPARATOR = '|'

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

