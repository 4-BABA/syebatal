package io.baba4.syebatal.bf

import io.baba4.syebatal.Battlefield
import io.baba4.syebatal.BfCell.*
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldEncodeTest : BattlefieldTest() {
    private fun testEncode(input: Battlefield, output: String) =
        assertEquals(actual = input.encode(), expected = output)


    @Test
    fun encodeEmptyBattlefield() = testEncode(
        input = Battlefield(size = 0) { EMPTY },
        output = "0|"
    )

    @Test
    fun encodeOneSizeBattlefield() = testEncode(
        input = Battlefield(size = 1) { EMPTY },
        output = "1|•"
    )

    @Test
    fun encodeTwoSizeBattlefield() = testEncode(
        input = Battlefield(size = 2) { EMPTY },
        output = "2|••••"
    )

    @Test
    fun encodeCustomBattlefield() = testEncode(
        input = customBattlefield,
        output = customSerialization
    )
}
