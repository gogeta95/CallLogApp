package com.example.calllogapp.repository

import com.example.calllogapp.model.CallRecord
import com.example.calllogapp.model.CurrentCall
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    fun setCurrentCall(phoneNumber: String, currentCallerName: String?)

    fun callEnded()

    fun getCurrentCall(): CurrentCall

    fun addCallRecord(record: CallRecord)

    fun getCallRecords(): List<CallRecord>
}