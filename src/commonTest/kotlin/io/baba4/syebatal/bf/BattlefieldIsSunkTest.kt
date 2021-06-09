package io.baba4.syebatal.bf

import io.baba4.syebatal.Battlefield
import io.baba4.syebatal.Ship
import io.baba4.syebatal.isSunk
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldIsSunkTest : BattlefieldTest() {
    private fun test(input: Pair<Battlefield, Ship>, output: Boolean) = assertEquals(
        actual = input.first.isSunk(input.second),
        expected = output
    )

    @Test
    fun sunkShip() = test(
        input = customBattlefield to sunkShip,
        output = true
    )

    @Test
    fun damagedShip() = test(
        input = customBattlefield to damagedShip,
        output = false
    )

    @Test
    fun wholeShip() = test(
        input = customBattlefield to wholeShip,
        output = false
    )
}
