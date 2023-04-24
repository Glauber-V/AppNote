package com.example.appnote.di

import android.content.Context
import androidx.room.Room
import com.example.appnote.data.database.AppNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppNoteDatabaseModule::class]
)
object TestAppNoteDatabaseModule {

    @Provides
    @Singleton
    fun provideAppNoteDatabase(@ApplicationContext context: Context): AppNoteDatabase {
        return Room.inMemoryDatabaseBuilder(context.applicationContext, AppNoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}