package io.baba4.syebatal.elm

import java.util.*

typealias StageId = String

fun generateStageId() = UUID.randomUUID().toString()


interface StageCloseListener {
    fun closeOnSuccess(stageId: StageId)
    fun closeOnFailure(stageId: StageId)
}

