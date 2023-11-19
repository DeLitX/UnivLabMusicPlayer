package com.delitx.univlabmusicplayer.model

import android.net.Uri
import kotlin.time.Duration

data class AudioMetadata(
    val authorName: String?,
    val duration: Duration,
    val albumTitle: String?,
    val albumImage: Uri?,

)
