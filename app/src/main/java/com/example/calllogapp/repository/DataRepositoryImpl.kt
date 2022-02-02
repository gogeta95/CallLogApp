package com.example.calllogapp.repository

import com.example.calllogapp.interactor.CurrentCallQueryWatcher
import com.example.calllogapp.model.CallRecord
import com.example.calllogapp.model.CurrentCall

class DataRepositoryImpl(
    private val currentCallQueryWatcher: CurrentCallQueryWatcher
) : DataRepository {

    private val records = mutableListOf<CallRecord>()
    private var currentCall = CurrentCall(ongoing = false)

    override fun setCurrentCall(phoneNumber: String, currentCallerName: String?) {
        currentCall = CurrentCall(ongoing = true, number = phoneNumber, name = currentCallerName)
    }

    override fun callEnded() {
        currentCall = CurrentCall(ongoing = false)
    }

    override fun getCurrentCall(): CurrentCall {
        currentCallQueryWatcher.onCurrentCallRequested()
        return currentCall
    }

    override fun addCallRecord(record: CallRecord) {
        //insert latest data on top
        records.add(0, record)
    }

    override fun getCallRecords() = records
}