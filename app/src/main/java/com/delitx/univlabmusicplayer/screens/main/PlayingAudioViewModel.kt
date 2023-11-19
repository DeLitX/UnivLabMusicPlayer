package com.delitx.univlabmusicplayer.screens.main

import androidx.lifecycle.ViewModel
import com.delitx.univlabmusicplayer.player_controller.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayingAudioViewModel @Inject constructor(
    private val playerController: PlayerController,
) : ViewModel() {

    fun changePlaybackState() {
        playerController.updateCurrentState { state ->
            state?.copy(isPlaying = !state.isPlaying)
        }
    }

    val currentPlaybackStateFlow = playerController.stateFlow
}
