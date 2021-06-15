package io.baba4.syebatal.elm

import io.baba4.syebatal.models.*
import io.baba4.syebatal.models.BfCell.*
import kotlin.test.Test


class ShootTest : StateMachineTest() {
    private val players = listOf(Player(name = "Ferdinand Wrangel"), Player(name = "Vladimir Schmidt"))

    private val initialState = State(
        currentPlayerIndex = 0,
        players = players,
        battlefields = listOf(
            Battlefield2(
                size = 3,
                ships = setOf(
                    Ship2(Point(row = 0, column = 0) to FILLED),
                    Ship2(Point(row = 2, column = 2) to FILLED),
                ),
                emptyPoints = emptySet()
            ),
            Battlefield2(
                size = 3,
                ships = setOf(
                    Ship2(Point(row = 0, column = 2) to FILLED),
                    Ship2(Point(row = 2, column = 0) to FILLED),
                ),
                emptyPoints = emptySet()
            )
        )
    )
    private val firstShotPoint = Point(row = 0, column = 2)
    private val stateAfterFirstShot = State(
        currentPlayerIndex = 1,
        players = players,
        battlefields = listOf(
            Battlefield2(
                size = 3,
                ships = setOf(
                    Ship2(Point(row = 0, column = 0) to FILLED),
                    Ship2(Point(row = 2, column = 2) to FILLED),
                ),
                emptyPoints = emptySet()
            ),
            Battlefield2(
                size = 3,
                ships = setOf(
                    Ship2(Point(row = 0, column = 2) to DAMAGED),
                    Ship2(Point(row = 2, column = 0) to FILLED),
                ),
                emptyPoints = setOf(
                    Point(row = 0, column = 1),
                    Point(row = 1, column = 1),
                    Point(row = 1, column = 2),
                )
            ),
        )
    )
    private val secondShotPoint = Point(row = 1, column = 1)
    private val stateAfterSecondShot = State(
        currentPlayerIndex = 0,
        players = players,
        battlefields = listOf(
            Battlefield2(
                size = 3,
                ships = setOf(
                    Ship2(Point(row = 0, column = 0) to FILLED),
                    Ship2(Point(row = 2, column = 2) to FILLED),
                ),
                emptyPoints = setOf(secondShotPoint)
            ),
            Battlefield2(
                size = 3,
                ships = setOf(
                    Ship2(Point(row = 0, column = 2) to DAMAGED),
                    Ship2(Point(row = 2, column = 0) to FILLED),
                ),
                emptyPoints = setOf(
                    Point(row = 0, column = 1),
                    Point(row = 1, column = 1),
                    Point(row = 1, column = 2),
                )
            ),
        )
    )

    @Test
    fun firstShoot() = test(
        initialState = initialState,
        action = Action.Shoot(firstShotPoint),
        expectedState = stateAfterFirstShot
    )

    @Test
    fun secondShoot() = test(
        initialState = stateAfterFirstShot,
        action = Action.Shoot(secondShotPoint),
        expectedState = stateAfterSecondShot
    )

    @Test
    fun firstAndSecondShoot() = test(
        initialState = initialState,
        actions = listOf(
            Action.Shoot(firstShotPoint),
            Action.Shoot(secondShotPoint),
        ),
        expectedState = stateAfterSecondShot
    )
}
