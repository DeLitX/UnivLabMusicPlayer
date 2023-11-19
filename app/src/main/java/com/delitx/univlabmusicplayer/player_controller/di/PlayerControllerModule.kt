package com.delitx.univlabmusicplayer.player_controller.di

import com.delitx.univlabmusicplayer.player_controller.PlayerController
import com.delitx.univlabmusicplayer.player_controller.PlayerControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerControllerModule {
    @Binds
    @Singleton
    abstract fun bindPlayerController(impl: PlayerControllerImpl): PlayerController
}
