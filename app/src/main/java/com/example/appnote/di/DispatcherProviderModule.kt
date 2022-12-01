package com.example.appnote.di

import com.example.appnote.data.dispatchers.DefaultDispatcherProvider
import com.example.appnote.data.dispatchers.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherProviderModule {

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}