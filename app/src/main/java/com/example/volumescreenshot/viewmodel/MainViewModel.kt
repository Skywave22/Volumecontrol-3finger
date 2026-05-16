package com.example.volumescreenshot.viewmodel

import android.app.Application
import android.media.AudioManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.volumescreenshot.util.PermissionManager
import com.example.volumescreenshot.util.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Main ViewModel for the Volume & Screenshot app.
 *
 * Manages:
 * 1. Permission status
 * 2. Volume level
 * 3. Settings state
 * 4. UI state
 */
class MainViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val permissionManager = PermissionManager(application)
    private val preferencesManager = PreferencesManager(application)
    private val audioManager = application.getSystemService(android.content.Context.AUDIO_SERVICE) as AudioManager

    // LiveData for UI state
    private val _permissionStatus = MutableLiveData<String>()
    val permissionStatus: LiveData<String> = _permissionStatus

    private val _volumeLevel = MutableLiveData<Int>()
    val volumeLevel: LiveData<Int> = _volumeLevel

    private val _maxVolume = MutableLiveData<Int>()
    val maxVolume: LiveData<Int> = _maxVolume

    private val _accessibilityEnabled = MutableLiveData<Boolean>()
    val accessibilityEnabled: LiveData<Boolean> = _accessibilityEnabled

    private val _overlayEnabled = MutableLiveData<Boolean>()
    val overlayEnabled: LiveData<Boolean> = _overlayEnabled

    init {
        Timber.d("MainViewModel initialized")
        updatePermissionStatus()
        updateVolumeLevel()
        updateAccessibilityStatus()
        updateOverlayStatus()
    }

    /**
     * Updates the permission status LiveData.
     */
    fun updatePermissionStatus() {
        val status = permissionManager.getPermissionStatusMessage()
        _permissionStatus.value = status
        Timber.d("Permission status updated: $status")
    }

    /**
     * Updates the volume level LiveData.
     */
    fun updateVolumeLevel() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        _volumeLevel.value = currentVolume
        _maxVolume.value = maxVolume

        Timber.d("Volume updated: $currentVolume/$maxVolume")
    }

    /**
     * Updates the accessibility service status.
     */
    fun updateAccessibilityStatus() {
        val isEnabled = permissionManager.isAccessibilityServiceEnabled()
        _accessibilityEnabled.value = isEnabled
        Timber.d("Accessibility status updated: $isEnabled")
    }

    /**
     * Updates the overlay status.
     */
    fun updateOverlayStatus() {
        val isEnabled = preferencesManager.isOverlayEnabled()
        _overlayEnabled.value = isEnabled
        Timber.d("Overlay status updated: $isEnabled")
    }

    /**
     * Increases the volume by one step.
     */
    fun increaseVolume() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        if (currentVolume < maxVolume) {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                currentVolume + 1,
                AudioManager.FLAG_SHOW_UI
            )
            updateVolumeLevel()
            Timber.d("Volume increased to: ${currentVolume + 1}")
        }
    }

    /**
     * Decreases the volume by one step.
     */
    fun decreaseVolume() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        if (currentVolume > 0) {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                currentVolume - 1,
                AudioManager.FLAG_SHOW_UI
            )
            updateVolumeLevel()
            Timber.d("Volume decreased to: ${currentVolume - 1}")
        }
    }

    /**
     * Sets the volume to a specific level.
     */
    fun setVolume(level: Int) {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val clampedLevel = level.coerceIn(0, maxVolume)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            clampedLevel,
            AudioManager.FLAG_SHOW_UI
        )
        updateVolumeLevel()
        Timber.d("Volume set to: $clampedLevel")
    }

    /**
     * Gets the current gesture sensitivity.
     */
    fun getGestureSensitivity(): Int {
        return preferencesManager.getGestureSensitivity()
    }

    /**
     * Sets the gesture sensitivity.
     */
    fun setGestureSensitivity(sensitivity: Int) {
        preferencesManager.setGestureSensitivity(sensitivity)
        Timber.d("Gesture sensitivity set to: $sensitivity")
    }

    /**
     * Gets the vibration feedback status.
     */
    fun isVibrationEnabled(): Boolean {
        return preferencesManager.isVibrationFeedbackEnabled()
    }

    /**
     * Sets the vibration feedback status.
     */
    fun setVibrationEnabled(enabled: Boolean) {
        preferencesManager.setVibrationFeedbackEnabled(enabled)
        Timber.d("Vibration feedback set to: $enabled")
    }

    /**
     * Checks if all required permissions are granted.
     */
    fun hasAllPermissions(): Boolean {
        return permissionManager.hasAllPermissions()
    }

    /**
     * Gets a list of missing permissions.
     */
    fun getMissingPermissions(): List<String> {
        return permissionManager.getMissingPermissions()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        Timber.d("MainViewModel cleared")
    }
}
