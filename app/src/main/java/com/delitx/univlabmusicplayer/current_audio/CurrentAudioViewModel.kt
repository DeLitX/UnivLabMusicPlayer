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

    fun selectPreviousAudio() {
        val queue = playerController.queueFlow.value
        val currentAudio = currentPlaybackStateFlow.value?.currentAudio ?: return
        val currentAudioIndex = queue.indexOfFirst { it.id == currentAudio.id }
        if (currentAudioIndex == -1 || currentAudioIndex == 0) return
        playerController.selectAudio(queue[currentAudioIndex - 1])
    }

    fun selectNextAudio() {
        val queue = playerController.queueFlow.value
        val currentAudio = currentPlaybackStateFlow.value?.currentAudio ?: return
        val currentAudioIndex = queue.indexOfFirst { it.id == currentAudio.id }
        if (currentAudioIndex == -1 || currentAudioIndex == queue.lastIndex) return
        playerController.selectAudio(queue[currentAudioIndex + 1])
    }

    fun changePlaybackState() {
        val newPlayingState = !(currentPlaybackStateFlow.value?.isPlaying ?: false)
        playerController.setPlaying(newPlayingState)
    }

    fun seekTo(progress: Float) {
        playerController.seekTo(progress = progress)
    }

    fun seekBy(delta: Long) {
        playerController.seekBy(delta)
    }
}
