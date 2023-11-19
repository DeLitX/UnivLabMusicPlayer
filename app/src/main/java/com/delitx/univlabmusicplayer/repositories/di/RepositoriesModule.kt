package com.delitx.univlabmusicplayer.repositories.di

import com.delitx.univlabmusicplayer.repositories.audio.AudioRepository
import com.delitx.univlabmusicplayer.repositories.audio.AudioRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindAudioRepository(impl: AudioRepositoryImpl): AudioRepository
}
