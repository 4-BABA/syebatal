package io.baba4.syebatal.bf2

import io.baba4.syebatal.models.Point
import io.baba4.syebatal.models.Battlefield2 as Battlefield
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldEncodeTest : BattlefieldTest() {
    private fun testEncode(input: Battlefield, output: String) =
        assertEquals(actual = input.encode(), expected = output)


    @Test
    fun encodeEmptyBattlefield() = testEncode(
        input = Battlefield(size = 0, ships = emptySet(), emptyPoints = emptySet()),
        output = "0|"
    )

    @Test
    fun encodeOneSizeBattlefield() = testEncode(
        input = Battlefield(size = 1, ships = emptySet(), emptyPoints = setOf(Point(row = 0, column = 0))),
        output = "1|•"
    )

    @Test
    fun encodeTwoSizeBattlefield() = testEncode(
        input = Battlefield(
            size = 2,
            ships = emptySet(),
            emptyPoints = setOf(
                Point(row = 0, column = 0),
                Point(row = 0, column = 1),
                Point(row = 1, column = 0),
                Point(row = 1, column = 1),
            )
        ),
        output = "2|••••"
    )

    @Test
    fun encodeCustomBattlefield() = testEncode(
        input = customBattlefield,
        output = customSerialization
    )
}
