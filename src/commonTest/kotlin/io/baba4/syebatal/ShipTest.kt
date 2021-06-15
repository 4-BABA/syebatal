package io.baba4.syebatal

import io.baba4.syebatal.models.Orientation
import io.baba4.syebatal.models.Point
import io.baba4.syebatal.models.Ship1
import io.baba4.syebatal.models.pointsAround
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


class ShipTest {
    private val size = 6
    private val horizontalShip = Ship1(
        Point(row = 2, column = 3),
        Point(row = 2, column = 4),
        Point(row = 2, column = 5),
    )
    private val verticalShip = Ship1(
        Point(row = 2, column = 3),
        Point(row = 3, column = 3),
        Point(row = 4, column = 3),
    )

    @Test
    fun orientationHorizontal() = assertEquals(
        actual = horizontalShip.orientation,
        expected = Orientation.HORIZONTAL
    )

    @Test
    fun orientationVertical() = assertEquals(
        actual = verticalShip.orientation,
        expected = Orientation.VERTICAL
    )

    @Test
    fun constructAngle() {
        assertFails {
            Ship1(
                Point(row = 1, column = 1),
                Point(row = 1, column = 2),
                Point(row = 2, column = 2),
            )
        }
    }

    @Test
    fun pointsAroundHorizontal() = assertEquals(
        actual = horizontalShip.pointsAround(size),
        expected = setOf(
            Point(row = 1, column = 2),
            Point(row = 1, column = 3),
            Point(row = 1, column = 4),
            Point(row = 1, column = 5),
            Point(row = 2, column = 2),
            Point(row = 3, column = 2),
            Point(row = 3, column = 3),
            Point(row = 3, column = 4),
            Point(row = 3, column = 5),
        )
    )

    @Test
    fun pointsAroundVertical() = assertEquals(
        actual = verticalShip.pointsAround(size),
        expected = setOf(
            Point(row = 1, column = 3),
            Point(row = 1, column = 4),
            Point(row = 2, column = 4),
            Point(row = 3, column = 4),
            Point(row = 4, column = 4),
            Point(row = 5, column = 4),
            Point(row = 5, column = 3),
            Point(row = 5, column = 2),
            Point(row = 4, column = 2),
            Point(row = 3, column = 2),
            Point(row = 2, column = 2),
            Point(row = 1, column = 2),
        )
    )
}
