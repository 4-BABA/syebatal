package io.baba4.syebatal

import io.baba4.syebatal.BfCell.*


data class Battlefield2(
    val size: Int,
    val ships: List<Ship2>,
    val emptyPoints: Set<Point>
) {
    fun encode(): String = "$size$SEPARATOR" + buildString {
        (0 until size).forEach { row ->
            (0 until size).forEach { column ->
                val point = Point(row, column)
                val shipCell = ships.map { it.decks.entries }.flatten().find { it.key == point }?.value
                val cell = when {
                    shipCell != null -> shipCell
                    point in emptyPoints -> EMPTY
                    else -> UNCHECKED
                }
                append(cell.symbol)
            }
        }
    }

    fun shotAt(point: Point): Battlefield2 {
        if (point in emptyPoints) {
            return this
        }
        val ship = ships.find { point in it.points }
        return when {
            ship != null && ship.decks[point] == FILLED -> {
                val newShip = Ship2(decks = ship.decks.filterKeys { it != point } + (point to DAMAGED))
                val newEmptyPoints =
                    if (newShip.isSunk) emptyPoints + newShip.pointsAround(size)
                    else emptyPoints
                copy(ships = ships - ship + newShip, emptyPoints = newEmptyPoints)
            }
            ship == null -> copy(emptyPoints = emptyPoints + point)
            else -> this
        }
    }

    companion object {
        private const val SEPARATOR = '|'

        fun decode(string: String): Battlefield2 {
            val (sizeString, content) = string.split(SEPARATOR, limit = 2)
            val size = sizeString.toInt()
            val ships: MutableList<Ship2> = mutableListOf()
            val emptyPoints: MutableSet<Point> = mutableSetOf()
            content.forEachIndexed { index, char ->
                val point = Point(index / size, index % size)
                when (val cell = BfCell.fromSymbol(char)) {
                    FILLED, DAMAGED -> {
                        val currentShip = ships.find { ship ->
                            point.pointsAround(size).any { it in ship.points }
                        }
                        if (currentShip == null) {
                            ships += Ship2(point to cell)
                        } else {
                            ships -= currentShip
                            ships += Ship2(currentShip.decks + (point to cell))
                        }
                    }
                    EMPTY -> emptyPoints += point
                    UNCHECKED -> { /* do nothing */
                    }
                }
            }
            return Battlefield2(size, ships, emptyPoints)
        }
    }
}
