package com.delitx.univlabmusicplayer.repositories.audio

import com.delitx.univlabmusicplayer.model.AudioElement
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor() : AudioRepository {

    override suspend fun getAudioList(): List<AudioElement> {
        return emptyList()
    }
}
