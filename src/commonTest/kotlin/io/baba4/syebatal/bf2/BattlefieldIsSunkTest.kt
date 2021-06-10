package io.baba4.syebatal.bf2

import io.baba4.syebatal.Ship2
import io.baba4.syebatal.isSunk
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldIsSunkTest : BattlefieldTest() {
    private fun test(input: Ship2, output: Boolean) = assertEquals(
        actual = input.isSunk,
        expected = output
    )

    @Test
    fun sunkShip() = test(
        input = sunkShip,
        output = true
    )

    @Test
    fun damagedShip() = test(
        input = damagedShip,
        output = false
    )

    @Test
    fun wholeShip() = test(
        input = wholeShip,
        output = false
    )
}
