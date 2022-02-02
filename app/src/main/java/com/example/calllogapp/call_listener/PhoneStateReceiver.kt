package com.example.calllogapp.call_listener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.calllogapp.interactor.CallLogInteractor

class PhoneStateReceiver(private val callLogInteractor: CallLogInteractor) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val phoneNumber = intent.extras?.getString("incoming_number") ?: return

        val state = intent.extras?.getString("state")
        if (state == STATE_OFFHOOK) {
            callLogInteractor.callStarted(phoneNumber)
        }

        if (state == STATE_IDLE) {
            callLogInteractor.callEnded(phoneNumber)
        }
    }

    companion object {
        private const val STATE_OFFHOOK = "OFFHOOK"
        private const val STATE_IDLE = "IDLE"
    }
}