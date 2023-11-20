package com.delitx.univlabmusicplayer

import androidx.media3.common.Player
import com.delitx.univlabmusicplayer.current_audio.CurrentAudioViewModel
import com.delitx.univlabmusicplayer.model.AudioElement
import com.delitx.univlabmusicplayer.model.AudioMetadata
import com.delitx.univlabmusicplayer.model.PlaybackState
import com.delitx.univlabmusicplayer.player_controller.PlayerControllerImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.time.Duration.Companion.minutes

class CurrentAudioViewModelTest {

    @Mock
    lateinit var player: Player

    lateinit var viewModel: CurrentAudioViewModel

    private val testQueue = listOf(
        AudioElement("1", "Audio 1", path = "", AudioMetadata(null, 1.minutes, null, null)),
        AudioElement("2", "Audio 2", path = "", AudioMetadata(null, 1.minutes, null, null)),
        AudioElement("3", "Audio 3", path = "", AudioMetadata(null, 1.minutes, null, null)),
    )

    private val testPlaybackState =
        PlaybackState(
            currentAudio = AudioElement("2", "Audio 2", path = "", AudioMetadata(null, 1.minutes, null, null)),
            isPlaying = true,
            progress = 0f,
        )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val playerController = PlayerControllerImpl(player)
        playerController.initializePlayback(testQueue, testPlaybackState.currentAudio)
        viewModel = CurrentAudioViewModel(playerController)
    }

    @Test
    fun testSelectPreviousAudio() = runTest {
        viewModel.selectPreviousAudio()
        assertEquals(testQueue[0], viewModel.currentPlaybackStateFlow.value?.currentAudio)
    }

    @Test
    fun testSelectNextAudio() = runTest {
        viewModel.selectNextAudio()
        assertEquals(testQueue[2], viewModel.currentPlaybackStateFlow.value?.currentAudio)
    }

    @Test
    fun testChangePlaybackState() = runTest {
        viewModel.changePlaybackState()
        assertEquals(false, viewModel.currentPlaybackStateFlow.value?.isPlaying)
        viewModel.changePlaybackState()
        assertEquals(true, viewModel.currentPlaybackStateFlow.value?.isPlaying)
    }

    @Test
    fun testSeekTo() = runTest {
        val testProgress = 0.5f
        viewModel.seekTo(testProgress)
        assertEquals(0.5f, viewModel.currentPlaybackStateFlow.value?.progress)
    }

    @Test
    fun testSeekBy() = runTest {
        val testDelta = 0.5.minutes
        viewModel.seekBy(testDelta.inWholeMilliseconds)
        assertEquals(0.5f, viewModel.currentPlaybackStateFlow.value?.progress)
    }
}
