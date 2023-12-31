package com.delitx.univlabmusicplayer.player_controller

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.delitx.univlabmusicplayer.model.AudioElement
import com.delitx.univlabmusicplayer.model.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

class PlayerControllerImpl @Inject constructor(
    private val player: Player,
) : PlayerController {

    private val _queueFlow = MutableStateFlow<List<AudioElement>>(emptyList())
    override val queueFlow: StateFlow<List<AudioElement>> = _queueFlow.asStateFlow()

    private val _stateFlow = MutableStateFlow<PlaybackState?>(null)
    override val stateFlow: StateFlow<PlaybackState?> = _stateFlow.asStateFlow()

    init {
        PeriodicListener {
            val currentAudio = _stateFlow.value?.currentAudio ?: return@PeriodicListener
            val progress = player.currentPosition.toFloat() / currentAudio.metadata.duration.inWholeMilliseconds
            _stateFlow.update { it?.copy(progress = progress) }
        }
        player.addListener(makePlayerListener())
    }

    override fun initializePlayback(queue: List<AudioElement>, initialAudio: AudioElement) {
        player.stop()
        player.clearMediaItems()
        val itemToPlayIndex = queue.indexOfFirst { it.id == initialAudio.id }
        if (itemToPlayIndex == -1) return

        val mediaItems = queue.map {
            MediaItem.Builder()
                .setUri(File(it.path).toUri())
                .setMediaId(it.id)
                .build()
        }
        player.setMediaItems(mediaItems)
        player.prepare()
        player.seekTo(itemToPlayIndex, 0L)
        player.playWhenReady = true
        _queueFlow.update { queue }
        _stateFlow.update { PlaybackState(isPlaying = true, progress = 0f, currentAudio = initialAudio) }
    }

    override fun selectAudio(audio: AudioElement) {
        val itemToPlayIndex = queueFlow.value.indexOfFirst { it.id == audio.id }
        if (itemToPlayIndex == -1) return
        _stateFlow.update { it?.copy(currentAudio = audio, progress = 0f) }
        player.seekTo(itemToPlayIndex, 0L)
    }

    override fun setPlaying(isPlaying: Boolean) {
        _stateFlow.update { it?.copy(isPlaying = isPlaying) }
        player.playWhenReady = isPlaying
    }

    override fun seekTo(progress: Float) {
        _stateFlow.update { it?.copy(progress = progress) }
        player.seekTo((player.duration * progress).toLong())
    }

    override fun seekBy(delta: Long) {
        val currentAudio = _stateFlow.value?.currentAudio ?: return
        val currentPosition = player.currentPosition
        val newPosition = (currentPosition + delta).coerceIn(0L, currentAudio.metadata.duration.inWholeMicroseconds)
        player.seekTo(newPosition)
        val progress = (newPosition).toFloat() / currentAudio.metadata.duration.inWholeMilliseconds
        _stateFlow.update { it?.copy(progress = progress.coerceIn(0f, 1f)) }
    }

    private fun makePlayerListener(): Player.Listener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val newElementId = mediaItem?.mediaId ?: return
            val newAudio = queueFlow.value.firstOrNull { it.id == newElementId } ?: return
            _stateFlow.update { it?.copy(currentAudio = newAudio, progress = 0f) }
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            _stateFlow.update { it?.copy(isPlaying = playWhenReady) }
        }
    }
}
