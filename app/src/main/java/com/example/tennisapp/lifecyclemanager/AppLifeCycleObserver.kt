package com.example.tennisapp.lifecyclemanager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver() : DefaultLifecycleObserver {
    private val _isInBackground = mutableStateOf(false)
    val isInBackground: State<Boolean> get() = _isInBackground

    override fun onStop(owner: LifecycleOwner) {
        _isInBackground.value = true
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        _isInBackground.value = true
    }

    override fun onStart(owner: LifecycleOwner) {
        _isInBackground.value = false
    }
}