package io.baba4.syebatal

import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldEncodeDecodeEquivalencyTest {
    @Test
    fun encodeDecodeEquivalencyTest() = repeat(times = 20) { size ->
        repeat(times = 5) {
            val bfMatrix = Battlefield(size) { _, _ -> BfCell.values().random() }
            assertEquals(actual = Battlefield.decode(bfMatrix.encode()), expected = bfMatrix)
        }
    }
}
