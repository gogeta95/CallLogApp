package com.example.calllogapp

import android.app.Application
import com.example.calllogapp.call_listener.phoneStateModule
import com.example.calllogapp.di.appModule
import com.example.calllogapp.ui.main.di.mainActivityModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class CallLogApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@CallLogApplication)
            modules(appModule, mainActivityModule, phoneStateModule)
        }
    }
}