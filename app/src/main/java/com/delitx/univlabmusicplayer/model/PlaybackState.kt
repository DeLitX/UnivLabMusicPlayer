package com.delitx.univlabmusicplayer.model

data class PlaybackState(
    val isPlaying: Boolean,
    val progress: Float,
    val currentAudio: AudioElement,
)
