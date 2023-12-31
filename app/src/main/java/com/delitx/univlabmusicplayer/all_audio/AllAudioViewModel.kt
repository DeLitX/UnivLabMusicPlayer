package com.delitx.univlabmusicplayer.all_audio

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.delitx.univlabmusicplayer.model.AudioElement
import com.delitx.univlabmusicplayer.model.PlaybackState
import com.delitx.univlabmusicplayer.player_controller.PlayerController
import com.delitx.univlabmusicplayer.repositories.audio.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AllAudioViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val player: ExoPlayer,
    private val playerController: PlayerController,
) : ViewModel() {
    private val _audioListFlow = MutableStateFlow<List<AudioElement>>(emptyList())
    val audioListFlow: StateFlow<List<AudioElement>> = _audioListFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val audioList = audioRepository.getAudioList()
            _audioListFlow.update { audioList }
        }
    }

    fun startPlaybackOfAudio(audio: AudioElement) {
        playerController.initializePlayback(audioListFlow.value, audio)
    }
}
