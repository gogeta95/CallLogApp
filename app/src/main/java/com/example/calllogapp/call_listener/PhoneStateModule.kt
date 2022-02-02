package com.example.calllogapp.call_listener

import android.content.IntentFilter
import org.koin.dsl.module

val phoneStateModule = module {
    single { PhoneStateReceiver(get()) }

    single { IntentFilter("android.intent.action.PHONE_STATE") }
}