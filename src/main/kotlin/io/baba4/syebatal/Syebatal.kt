@file:OptIn(ExperimentalSerializationApi::class)

package io.baba4.syebatal

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import io.ktor.websocket.serialization.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import java.time.Duration
import java.util.*

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

private fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        webSocket("/join") {
            registerPlayer(this)
            while (true) {
                val action = receiveDeserialized<Action>()

            }
        }
    }
}
