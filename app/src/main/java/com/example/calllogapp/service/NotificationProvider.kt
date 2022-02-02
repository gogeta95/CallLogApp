package com.example.calllogapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.calllogapp.R
import com.example.calllogapp.ui.main.MainActivity

private const val CHANNEL_SERVER_NOTIFICATION = "CHANNEL_SERVER_NOTIFICATION"

class NotificationProvider {
    companion object {

        fun createNotificationChannel(application: Application) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = application.getString(R.string.channel_name)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_SERVER_NOTIFICATION, name, importance)
                val notificationManager: NotificationManager =
                    application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        fun createNotification(application: Application): Notification {
            val pendingIntent = createPendingIntent(application)
            return NotificationCompat.Builder(application, CHANNEL_SERVER_NOTIFICATION)
                .setContentTitle(application.getText(R.string.app_name))
                .setContentText(application.getText(R.string.server_running))
                .setSmallIcon(R.drawable.ic_call)
                .setContentIntent(pendingIntent)
                .build()
        }

        private fun createPendingIntent(context: Context): PendingIntent =
            Intent(context, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
    }
}