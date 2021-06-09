package io.baba4.syebatal.bf

import io.baba4.syebatal.Battlefield
import io.baba4.syebatal.Point
import io.baba4.syebatal.Ship
import io.baba4.syebatal.findShip
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldFindShipTest : BattlefieldTest() {
    private fun test(input: Pair<Battlefield, Point>, output: Ship?) = assertEquals(
        actual = input.first.findShip(input.second),
        expected = output
    )

    @Test
    fun sunkShipCell() = test(
        input = customBattlefield to sunkShip.points.first(),
        output = sunkShip
    )

    @Test
    fun damagedShipCell() = test(
        input = customBattlefield to damagedShip.points.first(),
        output = damagedShip
    )

    @Test
    fun wholeShipCell() = test(
        input = customBattlefield to wholeShip.points.first(),
        output = wholeShip
    )

    @Test
    fun emptyCell() = test(
        input = customBattlefield to emptyCell,
        output = null
    )
}
