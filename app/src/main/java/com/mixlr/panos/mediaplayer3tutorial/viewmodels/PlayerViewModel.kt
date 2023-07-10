package com.mixlr.panos.mediaplayer3tutorial.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController
import com.mixlr.panos.mediaplayer3tutorial.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlayerViewState(
    val player: MediaController? = null
)

class PlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerViewState())
    val uiState: StateFlow<PlayerViewState> = _uiState.asStateFlow()

    fun setPlayer(value: MediaController) {
        Log.d(MainActivity.logTag, "PlayerViewModel setPlayer()")
        _uiState.update { currentState ->
            currentState.copy(
                player = value
            )
        }
    }
}
