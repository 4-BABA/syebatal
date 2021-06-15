package io.baba4.syebatal.bf2

import io.baba4.syebatal.models.BfCell.*
import io.baba4.syebatal.models.Point
import io.baba4.syebatal.models.Ship2
import io.baba4.syebatal.models.Battlefield2 as Battlefield
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldEncodeDecodeEquivalencyTest {
    @Test
    fun encodeDecodeEquivalencyTest() = repeat(times = 20) { index ->
        val battlefield = Battlefield(
            size = index + 1,
            ships = setOf(
                Ship2(Point(row = 0, column = 0) to FILLED)
            ),
            emptyPoints = emptySet(),
        )
        assertEquals(
            actual = io.baba4.syebatal.models.Battlefield2.decode(battlefield.encode()),
            expected = battlefield
        )
    }
}
