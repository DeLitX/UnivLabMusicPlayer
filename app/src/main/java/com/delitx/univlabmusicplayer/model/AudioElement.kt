package com.delitx.univlabmusicplayer.model

data class AudioElement(
    val id: String,
    val name: String,
    val path: String,
    val metadata: AudioMetadata,
)
