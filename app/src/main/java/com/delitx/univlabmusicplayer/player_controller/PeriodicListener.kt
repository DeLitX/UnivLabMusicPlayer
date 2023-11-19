package com.delitx.univlabmusicplayer.player_controller

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeriodicListener(
    private val period: Long = 200L,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherForCallback: CoroutineDispatcher = Dispatchers.Main,
    private val action: () -> Unit,
) {
    private var isActive = true
    private val scope = CoroutineScope(dispatcher)

    init {
        scope.launch {
            while (isActive) {
                withContext(dispatcherForCallback) {
                    action()
                }
                delay(period)
            }
        }
    }

    fun stop() {
        isActive = false
        scope.cancel()
    }
}
