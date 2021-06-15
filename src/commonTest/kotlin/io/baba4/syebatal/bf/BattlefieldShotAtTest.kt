package io.baba4.syebatal.bf

import io.baba4.syebatal.models.Point
import kotlin.test.Test
import kotlin.test.assertEquals
import io.baba4.syebatal.models.Battlefield1 as Battlefield


class BattlefieldShotAtTest : BattlefieldTest() {
    private fun test(input: Pair<Battlefield, Point>, output: Battlefield) = assertEquals(
        actual = input.first.shotAt(input.second),
        expected = output
    )

    @Test
    fun shotAtWholeShip() = test(
        input = customBattlefield to Point(row = 2, column = 1),
        output = io.baba4.syebatal.models.Battlefield1.decode(
            string = "5|" +
                    "•☒☒• " +
                    "•••• " +
                    "•☒•  " +
                    "•••  " +
                    "  ☐☐☒"
        )
    )

    @Test
    fun shotAtDamagedShip() = test(
        input = customBattlefield to Point(row = 4, column = 3),
        output = io.baba4.syebatal.models.Battlefield1.decode(
            string = "5|" +
                    "•☒☒• " +
                    "•••• " +
                    "•☐   " +
                    "     " +
                    "  ☐☒☒"
        )
    )

    @Test
    fun shotAtSunkShip() = test(
        input = customBattlefield to Point(row = 0, column = 1),
        output = customBattlefield
    )

    @Test
    fun shotAtEmptyCell() = test(
        input = customBattlefield to emptyCell,
        output = customBattlefield
    )

    @Test
    fun shotAtUncheckedCell() = test(
        input = customBattlefield to Point(row = 4, column = 0),
        output = io.baba4.syebatal.models.Battlefield1.decode(
            string = "5|" +
                    "•☒☒• " +
                    "•••• " +
                    "•☐   " +
                    "     " +
                    "• ☐☐☒"
        )
    )
}
