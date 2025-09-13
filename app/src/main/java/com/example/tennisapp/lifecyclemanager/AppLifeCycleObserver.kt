package com.example.tennisapp.lifecyclemanager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

// Observer that listens to app lifecycle events and tracks whether the app is in the background
class AppLifecycleObserver() : DefaultLifecycleObserver {

    // Backing state that holds whether the app is in the background
    private val _isInBackground = mutableStateOf(false)

    // Public immutable state to expose background status (read-only to other classes)
    val isInBackground: State<Boolean> get() = _isInBackground

    // Called when the app moves to the background (Activity no longer visible)
    override fun onStop(owner: LifecycleOwner) {
        _isInBackground.value = true
    }

    // Called when the Activity is being destroyed (ensure it's flagged as background)
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        _isInBackground.value = true
    }

    // Called when the app moves to the foreground (Activity becomes visible)
    override fun onStart(owner: LifecycleOwner) {
        _isInBackground.value = false
    }
}
