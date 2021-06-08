package io.baba4.syebatal

import io.baba4.syebatal.BfCell.*
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldDecodeTest : BattlefieldTest() {
    private fun testDecode(input: String, output: Battlefield) =
        assertEquals(actual = Battlefield.decode(input), expected = output)


    @Test
    fun decodeEmptyBattlefield() = testDecode(
        input = "0|",
        output = Battlefield(size = 0) { EMPTY }
    )

    @Test
    fun decodeOneSizeBattlefield() = testDecode(
        input = "1|•",
        output = Battlefield(size = 1) { EMPTY }
    )

    @Test
    fun decodeTwoSizeBattlefield() = testDecode(
        input = "2|••••",
        output = Battlefield(size = 2) { EMPTY }
    )

    @Test
    fun decodeCustomBattlefield() = testDecode(
        input = customSerialization,
        output = customBattlefield
    )
}
