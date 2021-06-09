package io.baba4.syebatal.bf

import io.baba4.syebatal.Battlefield
import io.baba4.syebatal.BfCell.*
import io.baba4.syebatal.Point
import io.baba4.syebatal.Ship


open class BattlefieldTest {
    protected val customBattlefield = Battlefield(size = 3) { (row, column) ->
        when (row to column) {
            1 to 2 -> UNCHECKED
            0 to 0, 1 to 0 -> EMPTY
            1 to 1,  2 to 0, 2 to 1 -> FILLED
            0 to 1, 0 to 2, 2 to 2 -> DAMAGED
            else -> throw IllegalArgumentException("Size should be 3.")
        }
    }
    protected val customSerialization = "3|•☒☒•☐ ☐☐☒"

    protected val sunkShip = Ship(
        Point(row = 0, column = 1),
        Point(row = 0, column = 2),
    )
    protected val damagedShip = Ship(
        Point(row = 2, column = 0),
        Point(row = 2, column = 1),
        Point(row = 2, column = 2),
    )
    protected val wholeShip = Ship(
        Point(row = 1, column = 1),
    )
}
