package com.mixlr.panos.mediaplayer3tutorial

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

class FantasticMessengerServiceConnection : ServiceConnection {
    private var boundService: Messenger? = null

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Log.d(MainActivity.logTag, "FantasticMessengerServiceConnection: onServiceConnected")
        boundService = Messenger(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d(MainActivity.logTag, "FantasticMessengerServiceConnection: onServiceDisconnected")
        boundService = null
    }

    fun sayHello() =
        boundService?.send(Message.obtain(null, FantasticMessengerService.MSG_SAY_HELLO, 0, 0))
}
