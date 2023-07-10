package com.mixlr.panos.mediaplayer3tutorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

private const val ONGOING_NOTIFICATION_CHANNEL_ID =
    "com.mixlr.panos.mediaplayer3tutorial.ongoing_notifications_channel"

class PlaybackService() : MediaSessionService() {
    private lateinit var channel: NotificationChannel
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        Log.d(MainActivity.logTag, "Creating PlaybackService")
        super.onCreate()
        prepareChannel()
        createMediaSession()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(MainActivity.logTag, "onBind()")
        return super.onBind(intent)
    }

    // Return a MediaSession to link with the MediaController that is making this request.
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        Log.d(MainActivity.logTag, "onGetSession(): packageName: ${controllerInfo.packageName}")

        return mediaSession
    }

    override fun onDestroy() {
        Log.d(MainActivity.logTag, "Destroying PlaybackService")
        stopForeground(STOP_FOREGROUND_REMOVE)
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
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

    private fun createMediaSession() {
        Log.d(MainActivity.logTag, "creating media session")

        val player = ExoPlayer.Builder(this).build()
        val customCallback = CustomMediaSessionCallback()
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(customCallback)
            .build()
    }
}
