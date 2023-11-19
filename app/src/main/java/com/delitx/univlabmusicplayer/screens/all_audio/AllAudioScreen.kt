package com.delitx.univlabmusicplayer.screens.all_audio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.delitx.univlabmusicplayer.R
import com.delitx.univlabmusicplayer.all_audio.AllAudioViewModel
import com.delitx.univlabmusicplayer.model.AudioElement

@Composable
fun AllAudioScreen(
    viewModel: AllAudioViewModel = hiltViewModel(),
) {
    val audioList by viewModel.audioListFlow.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(audioList) { audioItem ->
            AudioElementItem(
                audioItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.startPlaybackOfAudio(audioItem)
                    },
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun AudioElementItem(audioElement: AudioElement, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.then(
            Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        ),
    ) {
        GlideImage(
            model = audioElement.metadata.albumImage,
            contentDescription = null,
            modifier = Modifier.size(64.dp).clip(CircleShape),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_placeholder),
            failure = placeholder(R.drawable.ic_placeholder),
        )
        Column(modifier = Modifier.padding(start = 16.dp).align(Alignment.Top)) {
            Text(text = audioElement.name)
            val authorName = audioElement.metadata.authorName
            if (authorName != null) {
                Text(text = authorName, color = Color.Gray)
            }
        }
    }
}
