package com.example.calllogapp.ui.main

import com.example.calllogapp.model.CallRecord

data class UIModel(
    val startService: Boolean?,
    val serverRunning:Boolean,
    val serverUrl: String?,
    val records: List<CallRecord>
)