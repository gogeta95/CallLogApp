package com.example.calllogapp.server

import com.example.calllogapp.generateCallRecords
import com.example.calllogapp.model.CallRecord
import com.example.calllogapp.model.CurrentCall
import com.example.calllogapp.model.ServerInfo
import com.example.calllogapp.repository.DataRepository
import com.example.calllogapp.repository.ServerInfoProvider
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class CallLogServerImplTest {

    private val serverInfo = ServerInfo("start", emptyList())

    private val testDispatcher = StandardTestDispatcher()
    private val dataRepository: DataRepository = mock()
    private val serverInfoProvider: ServerInfoProvider = mock {
        on { getServerInfo() } doReturn serverInfo
    }

    private val server = CallLogServerImpl(testDispatcher, dataRepository, serverInfoProvider)

    @Test
    fun `WHEN server started THEN set start time`() {
        server.start()
        testDispatcher.scheduler.runCurrent()

        verify(serverInfoProvider).setStartTime()
    }

    @Test
    fun testRoot() {
        withTestApplication {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(serverInfo.toJson(), response.content)
            }
        }
    }

    @Test
    fun testStatus() {
        val currentCall = CurrentCall(true, "123", "name")

        whenever(dataRepository.getCurrentCall()) doReturn currentCall
        withTestApplication {
            handleRequest(HttpMethod.Get, "/status").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(currentCall.toJson(), response.content)
            }
        }
    }

    @Test
    fun testLog() {
        val callRecords = generateCallRecords(7)

        whenever(dataRepository.getCallRecords()) doReturn callRecords
        withTestApplication {
            handleRequest(HttpMethod.Get, "/log").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(callRecords.toJson(), response.content)
            }
        }
    }

    private fun withTestApplication(test: TestApplicationEngine.() -> TestApplicationCall) {
        withTestApplication(server.main, test)
    }

    companion object {
        private val gson = Gson()
        private fun Any.toJson() = gson.toJson(this)
    }
}