package io.baba4.syebatal.elm

interface Stage {
    val store: Elm.Store<*, *, *>
}

class StageStorage :
    MutableMap<StageId, Stage> by mutableMapOf(),
    StageCloseListener {

    override fun closeOnSuccess(stageId: StageId) {
        val stage = remove(stageId)
        stage?.store?.detach()
    }

    override fun closeOnFailure(stageId: StageId) {
        val stage = remove(stageId)
        stage?.store?.detach()
    }

    fun getPreparationStage(): PreparationStage {
        val preparationStage = values.filterIsInstance<PreparationStage>().find { it.isNotFull }
        if (preparationStage != null) return preparationStage

        return PreparationStage(closeListener = this)
            .also { this[it.roomId] = it }
            .also { it.store.attach() }
    }
}