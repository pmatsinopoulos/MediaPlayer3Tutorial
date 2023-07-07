package com.mixlr.panos.mediaplayer3tutorial

import android.os.Bundle
import android.util.Log
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val REWIND_30 = "RW by 30secs"
private const val FAST_FWD_30 = "FF by 30secs"
private const val MOVE_MS = 30_000 // milli seconds

class CustomMediaSessionCallback : MediaSession.Callback {
    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        Log.d(
            MainActivity.logTag,
            "CustomMediaSessionCallback onConnect(), packageName: ${controller.packageName}"
        )

        // Configure commands available to the controller
        val connectionResult = super.onConnect(session, controller)
        val sessionCommands = connectionResult.availableSessionCommands
            .buildUpon()
            .add(SessionCommand(REWIND_30, Bundle()))
            .add(SessionCommand(FAST_FWD_30, Bundle()))
            .build()
        return MediaSession.ConnectionResult.accept(
            sessionCommands,
            connectionResult.availablePlayerCommands
        )
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        Log.d(
            MainActivity.logTag,
            "CustomMediaSessionCallback onCustomCommand(): packageName: ${controller.packageName}"
        )

        if (customCommand.customAction in listOf(REWIND_30, FAST_FWD_30)) {
            var newPosition = session.player.currentPosition
            newPosition =
                if (customCommand.customAction == REWIND_30) newPosition - MOVE_MS else newPosition + MOVE_MS

            session.player.seekTo(newPosition)

            return Futures.immediateFuture(
                SessionResult(SessionResult.RESULT_SUCCESS)
            )
        }

        return super.onCustomCommand(session, controller, customCommand, args)
    }
}
