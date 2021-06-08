package io.baba4.syebatal

import kotlin.test.Test
import kotlin.test.assertEquals


class PointTest {
    private val size = 5
    private val centerPoint = Point(row = 2, column = 2)
    private val borderLeftPoint = Point(row = 2, column = 0)
    private val borderTopPoint = Point(row = 0, column = 2)
    private val borderRightPoint = Point(row = 2, column = 4)
    private val borderBottomPoint = Point(row = 4, column = 2)


    @Test
    fun leftValid() = assertEquals(
        actual = centerPoint.left(size),
        expected = Point(row = 2, column = 1)
    )

    @Test
    fun leftInvalid() = assertEquals(
        actual = borderLeftPoint.left(size),
        expected = null
    )

    @Test
    fun topValid() = assertEquals(
        actual = centerPoint.top(size),
        expected = Point(row = 1, column = 2)
    )

    @Test
    fun topInvalid() = assertEquals(
        actual = borderTopPoint.top(size),
        expected = null
    )

    @Test
    fun rightValid() = assertEquals(
        actual = centerPoint.right(size),
        expected = Point(row = 2, column = 3)
    )

    @Test
    fun rightInvalid() = assertEquals(
        actual = borderRightPoint.right(size),
        expected = null
    )

    @Test
    fun bottomValid() = assertEquals(
        actual = centerPoint.bottom(size),
        expected = Point(row = 3, column = 2)
    )

    @Test
    fun bottomInvalid() = assertEquals(
        actual = borderBottomPoint.bottom(size),
        expected = null
    )

    @Test
    fun topLeftValid() = assertEquals(
        actual = centerPoint.topLeft(size),
        expected = Point(row = 1, column = 1)
    )

    @Test
    fun topLeftInvalid() = assertEquals(
        actual = borderLeftPoint.topLeft(size),
        expected = null
    )

    @Test
    fun topRightValid() = assertEquals(
        actual = centerPoint.topRight(size),
        expected = Point(row = 1, column = 3)
    )

    @Test
    fun topRightInvalid() = assertEquals(
        actual = borderTopPoint.topRight(size),
        expected = null
    )

    @Test
    fun bottomLeftValid() = assertEquals(
        actual = centerPoint.bottomLeft(size),
        expected = Point(row = 3, column = 1)
    )

    @Test
    fun bottomLeftInvalid() = assertEquals(
        actual = borderBottomPoint.bottomLeft(size),
        expected = null
    )

    @Test
    fun bottomRightValid() = assertEquals(
        actual = centerPoint.bottomRight(size),
        expected = Point(row = 3, column = 3)
    )

    @Test
    fun bottomRightInvalid() = assertEquals(
        actual = borderRightPoint.bottomRight(size),
        expected = null
    )


    @Test
    fun pointsAroundCenter() = assertEquals(
        actual = centerPoint.pointsAround(size),
        expected = setOf(
            Point(row = 1, column = 1),
            Point(row = 1, column = 2),
            Point(row = 1, column = 3),
            Point(row = 2, column = 3),
            Point(row = 3, column = 3),
            Point(row = 3, column = 2),
            Point(row = 3, column = 1),
            Point(row = 2, column = 1),
        )
    )

    @Test
    fun pointsAroundBorderTop() = assertEquals(
        actual = borderTopPoint.pointsAround(size),
        expected = setOf(
            Point(row = 0, column = 1),
            Point(row = 1, column = 1),
            Point(row = 1, column = 2),
            Point(row = 1, column = 3),
            Point(row = 0, column = 3),
        )
    )
}
