package com.example.calllogapp.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.example.calllogapp.call_listener.PhoneStateReceiver
import com.example.calllogapp.server.CallLogServer
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject

private const val SERVER_NOTIFICATION_ID = 6

class ServerService : Service() {

    private var server: CallLogServer? = null
    private val phoneStateReceiver: PhoneStateReceiver by inject()
    private val phoneStateIntentFilter: IntentFilter by inject()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        running = true

        server = getKoin().get()
        server?.start()

        registerReceiver(phoneStateReceiver, phoneStateIntentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()

        return START_STICKY
    }

    private fun startForegroundService() {
        NotificationProvider.createNotificationChannel(application)
        val notification = NotificationProvider.createNotification(application)
        startForeground(SERVER_NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        server?.stop()
        server = null
        unregisterReceiver(phoneStateReceiver)
        running = false
    }

    companion object {
        var running = false
    }
}