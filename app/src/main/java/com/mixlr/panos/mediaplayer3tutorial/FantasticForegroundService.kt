package com.mixlr.panos.mediaplayer3tutorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

private const val ONGOING_NOTIFICATION_CHANNEL_ID =
    "com.mixlr.panos.mediaplayer3tutorial.ongoing_notifications_channel"

class FantasticForegroundService() : Service() {
    private lateinit var channel: NotificationChannel

    override fun onCreate() {
        Log.d(MainActivity.logTag, "Creating FantasticForegroundService")
        prepareChannel()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(MainActivity.logTag, "Starting FantasticForegroundService")

        val notification = NotificationCompat.Builder(
            this,
            ONGOING_NOTIFICATION_CHANNEL_ID
        ).apply {
            setOngoing(true) // the notification is non-dismissable by the user
            setSound(null)
            setSmallIcon(com.google.android.material.R.drawable.ic_m3_chip_check)
            setContentTitle("Not. from FG Service")
            setContentText("Notification from FG Service")
            setStyle(NotificationCompat.BigTextStyle().bigText("Notification from FG Service"))
        }.build()

        startForeground(1, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        // do not allow binding by returning +null+
        return null
    }

    override fun onDestroy() {
        Log.d(MainActivity.logTag, "Destroying FantasticForegroundService")
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    private fun prepareChannel() {
        val name = getText(R.string.channel_name)
        val descriptionText = getText(R.string.channel_description).toString()
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        channel =
            NotificationChannel(ONGOING_NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(null, null)
                enableLights(false)
                enableVibration(false)
            }
        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}
