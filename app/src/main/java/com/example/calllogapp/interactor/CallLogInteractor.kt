package com.example.calllogapp.interactor

interface CallLogInteractor {

    fun callStarted(phoneNumber: String)

    fun callEnded(phoneNumber: String)
}