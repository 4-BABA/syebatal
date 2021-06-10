package io.baba4.syebatal.bf2

import io.baba4.syebatal.BfCell
import io.baba4.syebatal.BfCell.*
import io.baba4.syebatal.Point
import io.baba4.syebatal.Ship2
import io.baba4.syebatal.Battlefield2 as Battlefield
import kotlin.test.Test
import kotlin.test.assertEquals


class BattlefieldEncodeDecodeEquivalencyTest {
    @Test
    fun encodeDecodeEquivalencyTest() = repeat(times = 20) { index ->
        val battlefield = Battlefield(
            size = index + 1,
            ships = listOf(
                Ship2(Point(row = 0, column = 0) to FILLED)
            ),
            emptyPoints = emptySet(),
        )
        assertEquals(actual = Battlefield.decode(battlefield.encode()), expected = battlefield)
    }
}
