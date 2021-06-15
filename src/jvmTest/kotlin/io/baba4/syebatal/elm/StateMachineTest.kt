package io.baba4.syebatal.elm

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.assertEquals


open class StateMachineTest {
    protected fun test(initialState: State, action: Action, expectedState: State) =
        test(initialState, listOf(action), expectedState)

    protected fun test(initialState: State, actions: List<Action>, expectedState: State) = runBlocking {
        val stateMachine = StateMachine(initialState)
        stateMachine.start()
        delay(timeMillis = 50) // wait time to start state machine coroutines
        actions.forEach { stateMachine.setAction(it) }
        delay(timeMillis = 50) // wait time to handle actions by state machine
        assertEquals(actual = stateMachine.currentState, expected = expectedState)
    }
}
