package com.delitx.univlabmusicplayer.player_controller

import com.delitx.univlabmusicplayer.model.AudioElement
import com.delitx.univlabmusicplayer.model.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PlayerControllerImpl @Inject constructor() : PlayerController {

    private val _queueFlow = MutableStateFlow<List<AudioElement>>(emptyList())
    override val queueFlow: StateFlow<List<AudioElement>> = _queueFlow.asStateFlow()

    private val _stateFlow = MutableStateFlow<PlaybackState>(PlaybackState(false, null))
    override val stateFlow: StateFlow<PlaybackState> = _stateFlow.asStateFlow()

    override fun updateCurrentState(update: (PlaybackState) -> PlaybackState) {
        _stateFlow.update(update)
    }

    override fun updateQueue(update: (List<AudioElement>) -> List<AudioElement>) {
        _queueFlow.update(update)
    }
}
