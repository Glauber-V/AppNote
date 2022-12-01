package com.example.appnote.di

import android.content.Context
import androidx.room.Room
import com.example.appnote.data.database.AppNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppNoteDatabaseModule {

    @Provides
    @Singleton
    fun provideAppNoteDatabase(@ApplicationContext context: Context): AppNoteDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppNoteDatabase::class.java,
            "appnote.db"
        ).build()
    }
}