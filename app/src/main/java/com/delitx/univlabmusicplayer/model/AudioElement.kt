package com.delitx.univlabmusicplayer.model

import android.net.Uri

data class AudioElement(
    val name: String,
    val uri: Uri,
    val metadata: AudioMetadata,
)
