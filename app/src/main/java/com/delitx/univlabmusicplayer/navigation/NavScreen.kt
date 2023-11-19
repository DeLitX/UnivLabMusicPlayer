package com.delitx.univlabmusicplayer.navigation

sealed class NavScreen {
    abstract val route: String
    abstract val routePrefix: String

    object AllAudio : NavScreen() {
        override val route: String = "all_audio_screen"
        override val routePrefix: String = route
    }

    object Playlists : NavScreen() {
        override val route: String = "playlists_screen"
        override val routePrefix: String = route
    }

    object CurrentAudio : NavScreen() {
        override val route: String = "current_audio_screen"
        override val routePrefix: String = route
    }

    data class Playlist(val playlistId: Int) : NavScreen() {
        override val route: String = "playlist_screen?playlist_id=$playlistId"
        override val routePrefix: String = "playlist_screen"
    }
}
