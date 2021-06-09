package io.baba4.syebatal.bf

import io.baba4.syebatal.Battlefield
import io.baba4.syebatal.BfCell.*
import io.baba4.syebatal.Point
import io.baba4.syebatal.Ship


open class BattlefieldTest {
    protected val customBattlefield = Battlefield(size = 5) { (row, column) ->
        when (row to column) {
            0 to 4,
            1 to 4,
            2 to 2, 2 to 3, 2 to 4,
            3 to 0, 3 to 1, 3 to 2, 3 to 3, 3 to 4,
            4 to 0, 4 to 1 -> UNCHECKED

            0 to 0, 0 to 3,
            1 to 0, 1 to 1, 1 to 2, 1 to 3,
            2 to 0 -> EMPTY

            2 to 1,
            4 to 2, 4 to 3 -> FILLED

            0 to 1, 0 to 2,
            4 to 4 -> DAMAGED
            else -> throw IllegalArgumentException("Size should be 5.")
        }
    }
    protected val customSerialization = "5|" +
            "•☒☒• " +
            "•••• " +
            "•☐   " +
            "     " +
            "  ☐☐☒"

    protected val emptyCell = Point(row = 0, column = 0)

    protected val sunkShip = Ship(
        Point(row = 0, column = 1),
        Point(row = 0, column = 2),
    )
    protected val damagedShip = Ship(
        Point(row = 4, column = 2),
        Point(row = 4, column = 3),
        Point(row = 4, column = 4),
    )
    protected val wholeShip = Ship(
        Point(row = 2, column = 1),
    )
}
