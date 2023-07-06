package com.mixlr.panos.mediaplayer3tutorial

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

class FantasticBoundServiceConnection : ServiceConnection {
    private lateinit var boundService: FantasticBoundService

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d(MainActivity.logTag, "FantasticBoundServiceConnection: onServiceConnected")
        val binder = service as FantasticBoundService.LocalBinder
        boundService = binder.getService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d(MainActivity.logTag, "FantasticBoundServiceConnection: onServiceDisconnected")
    }

    fun randomNumber() = boundService.randomNumber
}
