package com.delitx.univlabmusicplayer.screens.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.delitx.univlabmusicplayer.R
import com.delitx.univlabmusicplayer.navigation.NavScreen
import com.delitx.univlabmusicplayer.screens.all_audio.AllAudioScreen
import com.delitx.univlabmusicplayer.screens.current_audio.CurrentAudioScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) },
    ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
            NavGraph(navController)
        }
    }
}

@Composable
private fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavScreen.AllAudio.route) {
        composable(NavScreen.AllAudio.route) {
            AllAudioScreen()
        }
        composable(NavScreen.Playlists.route) {
            AllAudioScreen()
        }
        composable(
            NavScreen.CurrentAudio.route,
            enterTransition = {
                slideInVertically(initialOffsetY = { -it / 4 }) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -it / 4 }) + fadeOut()
            },
        ) {
            CurrentAudioScreen()
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isCurrentAudioScreen = currentRoute?.contains(NavScreen.CurrentAudio.routePrefix) ?: false
    val shouldBeVisible = !isCurrentAudioScreen
    AnimatedVisibility(
        visible = shouldBeVisible,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Column {
            PlayingAudioStatus(navController = navController)
            BottomNavigation(navController = navController)
        }
    }
}

@SuppressLint("PrivateResource")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PlayingAudioStatus(
    navController: NavHostController,
    viewModel: PlayingAudioViewModel = hiltViewModel(),
) {
    val currentPlaybackState by viewModel.currentPlaybackStateFlow.collectAsState()
    AnimatedVisibility(
        visible = currentPlaybackState != null,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    navController.navigate(NavScreen.CurrentAudio.route)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f),
            ) {
                val currentAudio = currentPlaybackState?.currentAudio
                if (currentAudio != null) {
                    GlideImage(
                        model = currentAudio.metadata.albumImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(36.dp)
                            .align(Alignment.CenterVertically)
                            .clip(CircleShape),
                    )
                    Column(modifier = Modifier.padding(start = 16.dp, top = 7.dp).align(Alignment.Top)) {
                        Text(
                            text = currentAudio.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = currentAudio.metadata.authorName ?: "",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(0.4f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(50.dp),
            ) {
                Icon(
                    painter = painterResource(
                        id = if (currentPlaybackState?.isPlaying == true) {
                            androidx.media3.ui.R.drawable.exo_icon_pause
                        } else {
                            androidx.media3.ui.R.drawable.exo_icon_play
                        },
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            viewModel.changePlaybackState()
                        }
                        .padding(7.dp)
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}

@Composable
private fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavigationItem(
            NavScreen.AllAudio,
            painterResource(id = R.drawable.ic_music),
            stringResource(id = R.string.bottom_navigation_tab_all_audio),
        ),
        BottomNavigationItem(
            NavScreen.Playlists,
            painterResource(id = R.drawable.ic_list),
            stringResource(id = R.string.bottom_navigation_tab_playlists),
        ),
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.4f),
                alwaysShowLabel = true,
                label = {
                    Text(
                        text = item.label,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.label,
                    )
                },
            )
        }
    }
}

private data class BottomNavigationItem(
    val screen: NavScreen,
    val icon: Painter,
    val label: String,
)
