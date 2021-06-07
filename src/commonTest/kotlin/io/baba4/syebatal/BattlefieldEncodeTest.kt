package io.baba4.syebatal

import io.baba4.syebatal.BfCell.*
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldEncodeTest : BattlefieldTest() {
    private fun testEncode(input: Battlefield, output: String) =
        assertEquals(actual = input.encode(), expected = output)


    @Test
    fun encodeEmptyBattlefield() = testEncode(
        input = Battlefield(size = 0) { _, _ -> EMPTY },
        output = "0|"
    )

    @Test
    fun encodeOneSizeBattlefield() = testEncode(
        input = Battlefield(size = 1) { _, _ -> EMPTY },
        output = "1|•"
    )

    @Test
    fun encodeTwoSizeBattlefield() = testEncode(
        input = Battlefield(size = 2) { _, _ -> EMPTY },
        output = "2|••••"
    )

    @Test
    fun encodeCustomBattlefield() = testEncode(
        input = customBattlefield,
        output = customSerialization
    )
}
