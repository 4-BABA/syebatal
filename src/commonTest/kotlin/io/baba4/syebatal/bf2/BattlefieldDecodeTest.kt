package io.baba4.syebatal.bf2

import io.baba4.syebatal.Point
import io.baba4.syebatal.Battlefield2 as Battlefield
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldDecodeTest : BattlefieldTest() {
    private fun testDecode(input: String, output: Battlefield) =
        assertEquals(actual = Battlefield.decode(input), expected = output)


    @Test
    fun decodeEmptyBattlefield() = testDecode(
        input = "0|",
        output = Battlefield(size = 0, ships = emptySet(), emptyPoints = emptySet())
    )

    @Test
    fun decodeOneSizeBattlefield() = testDecode(
        input = "1|•",
        output = Battlefield(size = 1, ships = emptySet(), emptyPoints = setOf(Point(row = 0, column = 0)))
    )

    @Test
    fun decodeTwoSizeBattlefield() = testDecode(
        input = "2|••••",
        output = Battlefield(
            size = 2,
            ships = emptySet(),
            emptyPoints = setOf(
                Point(row = 0, column = 0),
                Point(row = 0, column = 1),
                Point(row = 1, column = 0),
                Point(row = 1, column = 1),
            )
        )
    )

    @Test
    fun decodeCustomBattlefield() = testDecode(
        input = customSerialization,
        output = customBattlefield
    )
}
