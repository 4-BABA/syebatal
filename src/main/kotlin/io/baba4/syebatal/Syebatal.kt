@file:OptIn(ExperimentalSerializationApi::class)

package io.baba4.syebatal

import com.baba4.syebatal.models.Action
import com.baba4.syebatal.models.PlayerId
import io.baba4.syebatal.elm.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import java.time.Duration
import java.util.UUID

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val jsonConverter = KotlinxWebsocketSerializationConverter(Json)
val cborConverter = KotlinxWebsocketSerializationConverter(Cbor)

fun Application.module() {
    configureSockets()
    configureRouting()
}

private fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = jsonConverter
    }
}

val stageStorage = StageStorage()

private fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        webSocket("/join") {
            val playerId = registerPlayer()
            val preparationStage = stageStorage.getPreparationStage()
            while (true) {
                when (val action = receiveDeserialized<Action>()) {
                    is Action.Preparation -> preparationStage.store.setAction(action, playerId)
                    is Action.Placement -> TODO()
                    is Action.Game -> TODO()
                }
            }
        }
    }
}

fun registerPlayer(): PlayerId {
    // TODO: save player somewhere
    return (0L .. Long.MAX_VALUE).random()
}
