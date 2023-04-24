package com.example.appnote.dispatchers

import com.example.appnote.data.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@ExperimentalCoroutinesApi
class FakeDispatcherProvider : DispatcherProvider {

    override val main: CoroutineDispatcher
        get() = StandardTestDispatcher()

    override val io: CoroutineDispatcher
        get() = StandardTestDispatcher()
}