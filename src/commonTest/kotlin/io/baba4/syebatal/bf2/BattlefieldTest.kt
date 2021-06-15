package io.baba4.syebatal.bf2

import io.baba4.syebatal.models.Battlefield2 as Battlefield
import io.baba4.syebatal.models.BfCell.*
import io.baba4.syebatal.models.Point
import io.baba4.syebatal.models.Ship2


open class BattlefieldTest {
    protected val sunkShip = Ship2(
        Point(row = 0, column = 1) to DAMAGED,
        Point(row = 0, column = 2) to DAMAGED
    )
    protected val damagedShip = Ship2(
        Point(row = 4, column = 2) to FILLED,
        Point(row = 4, column = 3) to FILLED,
        Point(row = 4, column = 4) to DAMAGED
    )
    protected val wholeShip = Ship2(
        Point(row = 2, column = 1) to FILLED
    )

    protected val customBattlefield = Battlefield(
        size = 5,
        ships = setOf(sunkShip, wholeShip, damagedShip),
        emptyPoints = setOf(
            Point(row = 0, column = 0),
            Point(row = 0, column = 3),
            Point(row = 1, column = 0),
            Point(row = 1, column = 1),
            Point(row = 1, column = 2),
            Point(row = 1, column = 3),
            Point(row = 2, column = 0),
        )
    )
    protected val customSerialization = "5|" +
            "•☒☒• " +
            "•••• " +
            "•☐   " +
            "     " +
            "  ☐☐☒"

    protected val emptyCell = Point(row = 0, column = 0)
}
