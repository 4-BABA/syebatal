package io.baba4.syebatal.models

import io.baba4.syebatal.models.BfCell.*


data class Battlefield2(
    val size: Int,
    val ships: Set<Ship2>,
    val emptyPoints: Set<Point>
) {
    private fun traverseTable(block: (point: Point) -> Unit) {
        (0 until size).forEach { row ->
            (0 until size).forEach { column ->
                block(Point(row, column))
            }
        }
    }

    fun encode(): String = "$size$SEPARATOR" + buildString {
        val unifiedDecksMap = ships.toUnifiedDecksMap()
        traverseTable { point ->
            val shipCell = unifiedDecksMap[point]
            val cell = when {
                point in emptyPoints -> EMPTY
                shipCell != null -> shipCell
                else -> UNCHECKED
            }
            append(cell.symbol)
        }
    }

    fun shotAt(point: Point): Battlefield2 {
        val oldShip = ships.find { point in it.points }
        return when {
            point in emptyPoints -> this
            oldShip != null && oldShip.decks[point] == FILLED -> {
                val updatedShip =
                    Ship2(decks = oldShip.decks.filterKeys { it != point } + (point to DAMAGED))
                val updatedEmptyPoints =
                    if (updatedShip.isSunk) emptyPoints + updatedShip.pointsAround(size) else emptyPoints
                copy(ships = ships - oldShip + updatedShip, emptyPoints = updatedEmptyPoints)
            }
            oldShip == null -> copy(emptyPoints = emptyPoints + point)
            else -> this
        }
    }

    companion object {
        private const val SEPARATOR = '|'

        fun decode(string: String): Battlefield2 {
            val (sizeString, content) = string.split(SEPARATOR, limit = 2)
            val size = sizeString.toInt()
            val ships: MutableSet<Ship2> = mutableSetOf()
            val emptyPoints: MutableSet<Point> = mutableSetOf()
            content.forEachIndexed { index, char ->
                val point = Point(row = index / size, column = index % size)
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
                    UNCHECKED -> { /* do nothing */ }
                }
            }
            return Battlefield2(size, ships, emptyPoints)
        }
    }
}
