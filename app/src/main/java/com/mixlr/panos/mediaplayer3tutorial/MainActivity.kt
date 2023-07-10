package com.mixlr.panos.mediaplayer3tutorial

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mixlr.panos.mediaplayer3tutorial.databinding.ActivityMainBinding
import com.mixlr.panos.mediaplayer3tutorial.viewmodels.PlayerViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        const val logTag = "MediaPlayer3Tutorial"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var intentForSimpleService: Intent
    private lateinit var intentForPlaybackService: Intent
    private val grade = 10
    private var fantasticBoundServiceConnection: FantasticBoundServiceConnection? = null
    private var fantasticMessengerServiceConnection: FantasticMessengerServiceConnection? = null
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "MainActivity onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val playerViewModel: PlayerViewModel by viewModels()

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
            val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
            controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

            controllerFuture.addListener(
                {
                    Log.d(logTag, "MainActivity future: setting player")
                    playerViewModel.setPlayer(controllerFuture.get())
                },
                MoreExecutors.directExecutor()
            )
        }
        binding.btnStopForegroundService.setOnClickListener {
            if (::intentForPlaybackService.isInitialized) {
                stopService(intentForPlaybackService)
            }
            releaseController()
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

        createPlayer()

        binding.pvPlayer.player = player

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

        val videoPath = "file:///android_asset/sample-mp4-file.mp4"
        val videoUri = Uri.parse(videoPath)
        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)
        player.prepare()

    }

    override fun onStop() {
        Log.d(logTag, "MainActivity on Stop()")
        fantasticBoundServiceConnection?.let(::unbindService)
        fantasticMessengerServiceConnection?.let(::unbindService)
        releaseController()
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(logTag, "MainActivity onDestroy()")
        super.onDestroy()
    }

    private fun releaseController() {
        if (::controllerFuture.isInitialized) {
            Log.d(logTag, "Releasing controller")
            MediaController.releaseFuture(controllerFuture)
        }
    }

    private fun createPlayer() {
        Log.d(MainActivity.logTag, "MainActivity: createPlayer()")
        player = ExoPlayer.Builder(this).build()
    }
}
