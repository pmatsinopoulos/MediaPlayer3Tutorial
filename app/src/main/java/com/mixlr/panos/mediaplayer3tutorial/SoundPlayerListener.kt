package com.mixlr.panos.mediaplayer3tutorial

import android.util.Log
import androidx.media3.common.Player

class SoundPlayerListener : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        Log.d(MainActivity.logTag, "SoundPlayer playback state changed to $playbackState")
    }
}
