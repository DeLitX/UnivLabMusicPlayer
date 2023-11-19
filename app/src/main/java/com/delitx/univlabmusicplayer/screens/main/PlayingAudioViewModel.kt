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
        val isPlaying = playerController.stateFlow.value?.isPlaying ?: false
        playerController.setPlaying(!isPlaying)
    }

    val currentPlaybackStateFlow = playerController.stateFlow
}
