package com.delitx.univlabmusicplayer.repositories.audio

import com.delitx.univlabmusicplayer.model.AudioElement

interface AudioRepository {

    suspend fun getAudioList(): List<AudioElement>
}
