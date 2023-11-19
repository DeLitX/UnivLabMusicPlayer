package com.delitx.univlabmusicplayer.player_controller

import com.delitx.univlabmusicplayer.model.AudioElement
import com.delitx.univlabmusicplayer.model.PlaybackState
import kotlinx.coroutines.flow.StateFlow

interface PlayerController {

    val queueFlow: StateFlow<List<AudioElement>>
    val stateFlow: StateFlow<PlaybackState>

    fun updateCurrentState(update: (PlaybackState) -> PlaybackState)
}
