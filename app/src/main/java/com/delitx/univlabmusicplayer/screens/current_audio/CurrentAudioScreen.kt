package com.delitx.univlabmusicplayer.screens.current_audio

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.delitx.univlabmusicplayer.R
import com.delitx.univlabmusicplayer.current_audio.CurrentAudioViewModel
import com.delitx.univlabmusicplayer.model.PlaybackState
import kotlin.time.Duration.Companion.seconds
import androidx.media3.ui.R as exoR

@Composable
fun CurrentAudioScreen(
    viewModel: CurrentAudioViewModel = hiltViewModel(),
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        val currentPlaybackState by viewModel.currentPlaybackStateFlow.collectAsState()
        AnimatedVisibility(visible = currentPlaybackState == null) {
            NothingPlayingScreen()
        }
        AnimatedVisibility(visible = currentPlaybackState != null) {
            val currentState = currentPlaybackState
            if (currentState != null) {
                AudioPlayingScreen(
                    currentState,
                    onSeek = { viewModel.seekTo(it) },
                    onSelectPrevious = { viewModel.selectPreviousAudio() },
                    onSelectNext = { viewModel.selectNextAudio() },
                    onPlayPause = { viewModel.changePlaybackState() },
                    onFastForward = { viewModel.fastForward() },
                    onFastBackward = { viewModel.fastBackward() },
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun AudioPlayingScreen(
    currentPlaybackState: PlaybackState,
    onSeek: (Float) -> Unit,
    onSelectPrevious: () -> Unit,
    onSelectNext: () -> Unit,
    onPlayPause: () -> Unit,
    onFastForward: () -> Unit,
    onFastBackward: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            GlideImage(
                model = currentPlaybackState.currentAudio.metadata.albumImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit,
                loading = placeholder(R.drawable.ic_placeholder),
                failure = placeholder(R.drawable.ic_placeholder),
            )
        }
        BottomControls(
            currentPlaybackState,
            onSeek = onSeek,
            onSelectPrevious = onSelectPrevious,
            onSelectNext = onSelectNext,
            onPlayPause = onPlayPause,
            onFastForward = onFastForward,
            onFastBackward = onFastBackward,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary),
        )
    }
}

@SuppressLint("PrivateResource")
@Composable
private fun BottomControls(
    playbackState: PlaybackState,
    onSeek: (Float) -> Unit,
    onSelectPrevious: () -> Unit,
    onSelectNext: () -> Unit,
    onPlayPause: () -> Unit,
    onFastForward: () -> Unit,
    onFastBackward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = playbackState.currentAudio.name,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 20.dp, top = 10.dp, end = 20.dp),
        )
        Text(
            text = playbackState.currentAudio.metadata.authorName ?: "",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 20.dp, end = 20.dp, top = 5.dp),
        )
        Slider(
            value = playbackState.progress,
            onValueChange = onSeek,
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, end = 20.dp)
                .fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 5.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val currentSeconds =
                (playbackState.currentAudio.metadata.duration.inWholeSeconds * playbackState.progress).toInt()
            Text(
                text = currentSeconds.seconds.toString(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = playbackState.currentAudio.metadata.duration.toString(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                painter = painterResource(id = exoR.drawable.exo_ic_rewind),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
                    .clickable {
                        onFastBackward()
                    },
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Icon(
                painter = painterResource(id = exoR.drawable.exo_icon_previous),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
                    .clickable {
                        onSelectPrevious()
                    },
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Icon(
                painter = painterResource(id = if (playbackState.isPlaying) exoR.drawable.exo_icon_pause else exoR.drawable.exo_icon_play),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
                    .clickable {
                        onPlayPause()
                    },
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Icon(
                painter = painterResource(id = exoR.drawable.exo_icon_next),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
                    .clickable {
                        onSelectNext()
                    },
                tint = MaterialTheme.colorScheme.onPrimary,
            )
            Icon(
                painter = painterResource(id = exoR.drawable.exo_ic_forward),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
                    .clickable {
                        onFastForward()
                    },
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun NothingPlayingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.noting_playing),
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
