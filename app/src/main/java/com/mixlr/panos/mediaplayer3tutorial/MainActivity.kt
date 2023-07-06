package com.mixlr.panos.mediaplayer3tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.mixlr.panos.mediaplayer3tutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val logTag = "MediaPlayer3Tutorial"
    }

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaSession
    private lateinit var binding: ActivityMainBinding
    private lateinit var intentForSimpleService: Intent
    private lateinit var intentForForegroundService: Intent
    private val grade = 10
    private var fantasticBoundServiceConnection: FantasticBoundServiceConnection? = null
    private var fantasticMessengerServiceConnection: FantasticMessengerServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = ExoPlayer.Builder(this).build()
        session = MediaSession.Builder(this, player).build()

        binding.btnStartService.setOnClickListener {
            intentForSimpleService = Intent(this, FantasticService::class.java).also { intent ->
                intent.putExtra("grade", grade)
                startService(intent)
            }
        }
        binding.btnStopService.setOnClickListener {
            if (::intentForSimpleService.isInitialized) {
                stopService(intentForSimpleService)
            }
        }
        binding.btnStartForegroundService.setOnClickListener {
            intentForForegroundService = Intent(this, FantasticForegroundService::class.java)
            startForegroundService(intentForForegroundService)
        }
        binding.btnStopForegroundService.setOnClickListener {
            if (::intentForForegroundService.isInitialized) {
                stopService(intentForForegroundService)
            }
        }

        fantasticBoundServiceConnection = FantasticBoundServiceConnection()
        fantasticMessengerServiceConnection = FantasticMessengerServiceConnection()

        binding.btnRandomNumber.setOnClickListener {
            fantasticBoundServiceConnection?.let { fbsc ->
                binding.tvRandomNumber.text = fbsc.randomNumber().toString()
            }
        }

        binding.btnSayHello.setOnClickListener {
            fantasticMessengerServiceConnection?.sayHello()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d(logTag, "MainActivity on Start")

        Intent(this, FantasticBoundService::class.java).also { intent ->
            fantasticBoundServiceConnection?.let { fbsc ->
                bindService(
                    intent,
                    fbsc, Context.BIND_AUTO_CREATE
                )
            }
        }

        Intent(this, FantasticMessengerService::class.java).also { intent ->
            fantasticMessengerServiceConnection?.let { fmsc ->
                bindService(
                    intent,
                    fmsc,
                    Context.BIND_AUTO_CREATE
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        fantasticBoundServiceConnection?.let(::unbindService)
        fantasticMessengerServiceConnection?.let(::unbindService)
    }
}

