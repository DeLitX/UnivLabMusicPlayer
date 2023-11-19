package com.delitx.univlabmusicplayer.model

data class PlaybackState(
    val isPlaying: Boolean,
    val currentAudio: AudioElement,
)
