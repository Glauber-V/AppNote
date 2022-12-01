package com.example.appnote.di

import com.example.appnote.data.database.AppNoteDatabase
import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.repository.AppNoteRepository
import com.example.appnote.data.repository.DefaultAppNoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppNoteRepositoryModule {

    @Provides
    @Singleton
    fun provideAppNoteRepository(
        appNoteDatabase: AppNoteDatabase,
        dispatcherProvider: DispatcherProvider
    ): AppNoteRepository {
        return DefaultAppNoteRepository(appNoteDatabase, dispatcherProvider)
    }
}