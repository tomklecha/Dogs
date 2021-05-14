package com.tkdev.dogs.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

interface CommonCoroutineDispatcher {

    val IO: CoroutineContext

    val UI: CoroutineContext
}

open class CommonCoroutineDispatcherDefault : CommonCoroutineDispatcher {
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
