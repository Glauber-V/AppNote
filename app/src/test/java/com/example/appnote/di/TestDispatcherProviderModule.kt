package com.example.appnote.di

import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.dispatchers.FakeDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatcherProviderModule::class])
object TestDispatcherProviderModule {

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatcherProvider {
        return FakeDispatcherProvider()
    }
}