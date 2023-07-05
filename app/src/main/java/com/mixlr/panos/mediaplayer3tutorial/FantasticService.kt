package com.mixlr.panos.mediaplayer3tutorial

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class FantasticService() : Service() {
    override fun onCreate() {
        Log.d(MainActivity.logTag, "Creating FantasticService")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(MainActivity.logTag, "Starting Fantastic Service")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        // do not allow binding by returning +null+
        return null
    }

    override fun onDestroy() {
        Log.d(MainActivity.logTag, "Destroying Fantastic Service")
        super.onDestroy()
    }
}
