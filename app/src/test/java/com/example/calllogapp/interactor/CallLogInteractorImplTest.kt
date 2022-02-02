package com.example.calllogapp.interactor

import com.example.calllogapp.model.CallRecord
import com.example.calllogapp.repository.ContactsRepository
import com.example.calllogapp.repository.DataRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class CallLogInteractorImplTest {

    private val contactsRepository: ContactsRepository = mock()
    private val dataRepository: DataRepository = mock()
    private val testDispatcher = StandardTestDispatcher()
    private val currentCallQueryWatcher: CurrentCallQueryWatcher = mock()

    private val interactor = CallLogInteractorImpl(
        contactsRepository, dataRepository, testDispatcher, currentCallQueryWatcher
    )

    @Test
    fun `WHEN call started THEN notify currentCallQueryWatcher`() = runBlocking {
        interactor.callStarted("123")
        testDispatcher.scheduler.runCurrent()

        verify(currentCallQueryWatcher).onCallStarted()
        verify(dataRepository).setCurrentCall("123", null)
    }

    @Test
    fun `WHEN call started THEN set current call`() = runBlocking {
        whenever(contactsRepository.getNameForNumber("123")) doReturn "name"

        interactor.callStarted("123")
        testDispatcher.scheduler.runCurrent()

        verify(dataRepository).setCurrentCall("123", "name")
    }

    @Test
    fun `WHEN call completed THEN notify data repository`() = runBlocking {
        interactor.callStarted("123")
        testDispatcher.scheduler.runCurrent()

        interactor.callEnded("123")
        testDispatcher.scheduler.runCurrent()

        verify(dataRepository).callEnded()
    }

    @Test
    fun `WHEN call completed THEN add call record`(): Unit = runBlocking {
        testCallCompleted("123", "name 2", 4, 2)
    }

    @Test
    fun `WHEN multiple call completed THEN add call records`(): Unit = runBlocking {
        testCallCompleted("123", "name 1", 4, 2)
        testCallCompleted("12345", "name 2", 7, 6)
        testCallCompleted("5754634", "name 3", 10, 4)
    }

    private fun testCallCompleted(
        number: String,
        name: String,
        queryCount: Int,
        durationSeconds: Int
    ) {
        reset(dataRepository)
        whenever(contactsRepository.getNameForNumber(number)) doReturn name
        whenever(currentCallQueryWatcher.getCurrentCallQueryCount()) doReturn queryCount

        interactor.callStarted(number)
        testDispatcher.scheduler.runCurrent()
        //This is to test duration
        Thread.sleep(durationSeconds * 1000L)

        interactor.callEnded(number)
        testDispatcher.scheduler.runCurrent()

        argumentCaptor<CallRecord> {
            verify(dataRepository).addCallRecord(capture())

            assertEquals(firstValue.number, number)
            assertEquals(firstValue.name, name)
            assertEquals(firstValue.timesQueried, queryCount)
            assertEquals(firstValue.duration, durationSeconds.toString())
        }
    }
}