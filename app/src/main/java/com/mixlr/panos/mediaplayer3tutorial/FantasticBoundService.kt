package com.mixlr.panos.mediaplayer3tutorial

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.Random

class FantasticBoundService : Service() {
    private var binder = LocalBinder()

    private val mGenerator = Random()

    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    inner class LocalBinder : Binder() {
        fun getService(): FantasticBoundService = this@FantasticBoundService
    }

    override fun onCreate() {
        Log.d(MainActivity.logTag, "Creating FantasticBoundService")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(MainActivity.logTag, "Starting FantasticBoundService")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        Log.d(MainActivity.logTag, "Destroying FantasticBoundService")
        super.onDestroy()
    }
}
