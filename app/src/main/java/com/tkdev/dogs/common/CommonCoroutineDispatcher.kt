package com.tkdev.dogs.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

interface CommonCoroutineDispatcher {

    val IO: CoroutineContext

    val UI: CoroutineContext
}

@Singleton
open class CommonCoroutineDispatcherDefault @Inject constructor()
    : CommonCoroutineDispatcher {
    override val IO: CoroutineContext
        get() = Dispatchers.IO

    override val UI: CoroutineContext
        get() = Dispatchers.Main
}

@ExperimentalCoroutinesApi
class TestCoroutineDispatcherDefault : CommonCoroutineDispatcherDefault() {

    override val IO: CoroutineContext
        get() = Unconfined

    override val UI: CoroutineContext
        get() = Unconfined
}
