package io.baba4.syebatal.bf

import io.baba4.syebatal.models.Battlefield1 as Battlefield
import io.baba4.syebatal.models.BfCell
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldEncodeDecodeEquivalencyTest {
    @Test
    fun encodeDecodeEquivalencyTest() = repeat(times = 20) { size ->
        repeat(times = 5) {
            val battlefield = Battlefield(size) { BfCell.values().random() }
            assertEquals(
                actual = io.baba4.syebatal.models.Battlefield1.decode(battlefield.encode()),
                expected = battlefield
            )
        }
    }
}
