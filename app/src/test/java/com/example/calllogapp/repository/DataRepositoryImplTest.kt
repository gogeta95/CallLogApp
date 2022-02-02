package com.example.calllogapp.repository

import com.example.calllogapp.generateCallRecords
import com.example.calllogapp.interactor.CurrentCallQueryWatcher
import com.example.calllogapp.model.CallRecord
import junit.framework.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DataRepositoryImplTest {

    private val currentCallQueryWatcher: CurrentCallQueryWatcher = mock()

    private val repository = DataRepositoryImpl(currentCallQueryWatcher)

    @Test
    fun `WHEN current call not ongoing THEN empty`() {
        val call = repository.getCurrentCall()
        verify(currentCallQueryWatcher).onCurrentCallRequested()
        assertFalse(call.ongoing)
        assertNull(call.name)
        assertNull(call.number)
    }

    @Test
    fun `WHEN current call ongoing THEN correct data`() {
        repository.setCurrentCall("123", "name")
        val call = repository.getCurrentCall()
        verify(currentCallQueryWatcher).onCurrentCallRequested()
        assertTrue(call.ongoing)
        assertEquals(call.name, "name")
        assertEquals(call.number, "123")

        repository.callEnded()

        val call2 = repository.getCurrentCall()
        assertFalse(call2.ongoing)
        assertNull(call2.name)
        assertNull(call2.number)
    }

    @Test
    fun `WHEN add record THEN added on top`() {
        val callRecords = generateCallRecords(4)

        callRecords.forEach {
            repository.addCallRecord(it)
        }

        assertEquals(callRecords.reversed(), repository.getCallRecords())
    }
}