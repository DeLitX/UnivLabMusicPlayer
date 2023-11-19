package com.delitx.univlabmusicplayer.current_audio

import androidx.lifecycle.ViewModel
import com.delitx.univlabmusicplayer.player_controller.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentAudioViewModel @Inject constructor(
    private val playerController: PlayerController,
) : ViewModel() {
    val currentPlaybackStateFlow = playerController.stateFlow
}
