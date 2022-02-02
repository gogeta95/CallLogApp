package com.example.calllogapp.server

import androidx.annotation.VisibleForTesting
import com.example.calllogapp.repository.DataRepository
import com.example.calllogapp.repository.PORT
import com.example.calllogapp.repository.ServerInfoProvider
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CallLogServerImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataRepository: DataRepository,
    private val serverInfoProvider: ServerInfoProvider
) : CallLogServer {

    @VisibleForTesting
    val main: Application.() -> Unit = {
        install(ContentNegotiation) {
            gson {}
        }
        routing {
            get("/") {
                call.respond(serverInfoProvider.getServerInfo())
            }

            get("/status") {
                call.respond(dataRepository.getCurrentCall())
            }

            get("/log") {
                call.respond(dataRepository.getCallRecords())
            }
        }
    }

    private val server = embeddedServer(Netty, PORT, watchPaths = emptyList(), module = main)

    override fun start() {
        CoroutineScope(ioDispatcher).launch {
            serverInfoProvider.setStartTime()
            server.start(wait = false)
        }
    }

    override fun stop() {
        CoroutineScope(ioDispatcher).launch {
            server.stop(100, 100)
        }
    }
}