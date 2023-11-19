package com.delitx.univlabmusicplayer.navigation

sealed class NavScreen {
    abstract val route: String

    object AllAudio : NavScreen() {
        override val route: String = "all_audio_screen"
    }

    object Playlists : NavScreen() {
        override val route: String = "playlists_screen"
    }

    object CurrentAudio : NavScreen() {
        override val route: String = "current_audio_screen"
    }

    data class Playlist(val playlistId: Int) : NavScreen() {
        override val route: String = "playlist_screen?playlist_id=$playlistId"
    }
}
