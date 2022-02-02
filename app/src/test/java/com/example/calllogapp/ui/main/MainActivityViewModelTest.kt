package com.example.calllogapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calllogapp.generateCallRecords
import com.example.calllogapp.model.CallRecord
import com.example.calllogapp.repository.DataRepository
import com.example.calllogapp.repository.ServerInfoProvider
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*


class MainActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val url = "url"
    private val serverInfoProvider: ServerInfoProvider = mock {
        on { getServerURl() } doReturn url
    }
    private val dataRepository: DataRepository = mock {
        on { getCallRecords() } doReturn callRecords
    }

    private val viewModel = MainActivityViewModel(serverInfoProvider, dataRepository)

    private val initialState = UIModel(
        startService = null,
        serverRunning = false,
        serverUrl = null,
        records = callRecords
    )

    @Test
    fun `GIVEN service running WHEN service button clicked THEN update state`() {
        assertInitalState()

        viewModel.onServiceButtonClicked(serviceRunning = true)

        assertEquals(
            UIModel(
                startService = false,
                serverRunning = false,
                serverUrl = null,
                records = callRecords
            ),
            viewModel.observeUIModel().value
        )

        verify(dataRepository).getCallRecords()
    }

    @Test
    fun `GIVEN service not running WHEN service button clicked THEN update state`() {
        assertInitalState()

        viewModel.onServiceButtonClicked(serviceRunning = false)

        assertEquals(
            UIModel(
                startService = true,
                serverRunning = true,
                serverUrl = url,
                records = callRecords
            ),
            viewModel.observeUIModel().value
        )

        verify(dataRepository).getCallRecords()
        verify(serverInfoProvider).getServerURl()
    }

    @Test
    fun `GIVEN service running WHEN updateServiceStatus THEN update state`() {
        assertInitalState()

        viewModel.updateServiceStatus(serviceRunning = true)

        assertEquals(
            UIModel(
                startService = null,
                serverRunning = true,
                serverUrl = url,
                records = callRecords
            ),
            viewModel.observeUIModel().value
        )

        verify(dataRepository, times(2)).getCallRecords()
        verify(serverInfoProvider).getServerURl()
    }

    private fun assertInitalState() {
        assertEquals(
            initialState,
            viewModel.observeUIModel().value
        )
    }

    @Test
    fun `GIVEN service not running WHEN updateServiceStatus THEN update state`() {
        assertInitalState()

        viewModel.updateServiceStatus(serviceRunning = false)

        assertEquals(
            UIModel(
                startService = null,
                serverRunning = false,
                serverUrl = null,
                records = callRecords
            ),
            viewModel.observeUIModel().value
        )

        verify(dataRepository, times(2)).getCallRecords()
    }

    @Test
    fun `WHEN screen resumed THEN refresh data`() {
        assertInitalState()

        testResumed(emptyList())
        testResumed(
            listOf(
                CallRecord(
                    beginning = "beginning",
                    duration = "duration",
                    number = "number",
                    name = "name",
                    timesQueried = 3
                )
            )
        )
        testResumed(callRecords)
        testResumed(emptyList())

        verify(dataRepository, times(5)).getCallRecords()
    }

    private fun testResumed(list: List<CallRecord>) {
        whenever(dataRepository.getCallRecords()) doReturn list
        viewModel.onResume()

        assertEquals(
            UIModel(
                startService = null,
                serverRunning = false,
                serverUrl = null,
                records = list
            ),
            viewModel.observeUIModel().value
        )
    }

    companion object {
        val callRecords = generateCallRecords(5)
    }
}