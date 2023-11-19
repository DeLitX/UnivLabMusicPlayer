package com.delitx.univlabmusicplayer.all_audio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delitx.univlabmusicplayer.model.AudioElement
import com.delitx.univlabmusicplayer.repositories.audio.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllAudioViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val audioRepository: AudioRepository,
) : ViewModel() {
    private val _audioListFlow = MutableStateFlow<List<AudioElement>>(emptyList())
    val audioListFlow: StateFlow<List<AudioElement>> = _audioListFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val audioList = audioRepository.getAudioList()
            _audioListFlow.update { audioList }
        }
    }
}
