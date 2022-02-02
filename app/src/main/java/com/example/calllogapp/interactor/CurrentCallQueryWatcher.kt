package com.example.calllogapp.interactor

interface CurrentCallQueryWatcher {

    fun onCallStarted()

    fun onCurrentCallRequested()

    fun getCurrentCallQueryCount(): Int
}