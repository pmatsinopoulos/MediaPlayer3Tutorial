package com.mixlr.panos.mediaplayer3tutorial

import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.exoplayer.ExoPlayer
import com.mixlr.panos.mediaplayer3tutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val logTag = "MediaPlayer3Tutorial"
    }

    private lateinit var player: ExoPlayer
    private lateinit var session: MediaSession
    private lateinit var binding: ActivityMainBinding
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = ExoPlayer.Builder(this).build()
        session = MediaSession.Builder(this, player).build()

        binding.btnStartService.setOnClickListener {
            intent = Intent(this, FantasticService::class.java).also { intent ->
                startService(intent)
            }
        }
        binding.btnStopService.setOnClickListener {
            stopService(intent)
        }
    }
}