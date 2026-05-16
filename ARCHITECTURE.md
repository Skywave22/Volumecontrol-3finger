# Architecture Documentation

## Overview

This document describes the architecture and design patterns used in the Volume & Screenshot Utility app.

## Design Principles

1. **MVVM Architecture**: Separation of concerns between UI, business logic, and data
2. **Kotlin Best Practices**: Coroutines, extension functions, and null safety
3. **Android Best Practices**: Proper lifecycle management, permission handling
4. **Clean Code**: Clear naming, comprehensive comments, modular design
5. **Battery Efficiency**: Minimal resource usage, proper cleanup

## Architecture Layers

```
┌─────────────────────────────────────────┐
│         UI Layer (Activities)            │
│  MainActivity, SettingsActivity          │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      ViewModel Layer (MVVM)              │
│  MainViewModel                           │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│    Business Logic Layer (Services)       │
│  VolumeQuickSettingsTile                 │
│  GestureAccessibilityService             │
│  ScreenshotBackgroundService             │
│  FloatingOverlayService                  │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Utility Layer (Managers)            │
│  ScreenshotManager                       │
│  PreferencesManager                      │
│  PermissionManager                       │
│  BatteryOptimizer                        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Data Layer (System APIs)            │
│  SharedPreferences                       │
│  MediaProjection API                     │
│  AudioManager                            │
│  AccessibilityService                    │
└─────────────────────────────────────────┘
```

## Component Responsibilities

### UI Layer

#### MainActivity
- **Responsibility**: Main app entry point and settings UI
- **Lifecycle**: Activity lifecycle management
- **Interactions**: 
  - Displays permission status
  - Provides toggles for Accessibility Service and Overlay
  - Navigates to Settings Activity
- **Dependencies**: PermissionManager, PreferencesManager, MainViewModel

#### SettingsActivity
- **Responsibility**: App settings and configuration
- **Features**:
  - Gesture sensitivity slider
  - Vibration feedback toggle
  - Gesture detection status display
- **Dependencies**: PreferencesManager

### ViewModel Layer

#### MainViewModel
- **Responsibility**: Manage app state and business logic
- **Features**:
  - Volume level tracking
  - Permission status monitoring
  - Settings management
- **LiveData Exposed**:
  - `permissionStatus`: Current permission state
  - `volumeLevel`: Current volume level
  - `maxVolume`: Maximum volume level
  - `accessibilityEnabled`: Accessibility service status
  - `overlayEnabled`: Overlay status
- **Methods**:
  - Volume control (increase, decrease, set)
  - Settings management
  - Permission checking

### Service Layer

#### VolumeQuickSettingsTile
- **Type**: TileService (Android 6.0+)
- **Responsibility**: Quick Settings volume control
- **Features**:
  - Volume level cycling on tap
  - Visual state updates
  - Standard Android volume UI display
- **Lifecycle**: Managed by system
- **Permissions**: BIND_QUICK_SETTINGS_TILE

#### GestureAccessibilityService
- **Type**: AccessibilityService
- **Responsibility**: 3-finger swipe gesture detection
- **Features**:
  - Gesture event detection
  - Haptic feedback on detection
  - Screenshot service triggering
- **Lifecycle**: User-enabled in Settings
- **Permissions**: BIND_ACCESSIBILITY_SERVICE

#### ScreenshotBackgroundService
- **Type**: Service (Foreground Service on Android 8.0+)
- **Responsibility**: Screenshot capture and storage
- **Features**:
  - MediaProjection-based screen capture
  - Bitmap to file conversion
  - Gallery integration
  - Resource cleanup
- **Lifecycle**: Started by GestureAccessibilityService
- **Permissions**: FOREGROUND_SERVICE, FOREGROUND_SERVICE_MEDIA_PROJECTION

#### FloatingOverlayService
- **Type**: Service
- **Responsibility**: Floating volume control UI
- **Features**:
  - Draggable overlay widget
  - Volume buttons and slider
  - Window manager integration
- **Lifecycle**: Started/stopped by MainActivity
- **Permissions**: SYSTEM_ALERT_WINDOW

### Utility Layer

#### ScreenshotManager
- **Responsibility**: Handle screenshot capture and storage
- **Key Methods**:
  - `captureScreenshot()`: Capture screen using MediaProjection
  - `imageToBitmap()`: Convert Image to Bitmap
  - `saveBitmapToGallery()`: Save to device storage
  - `cleanup()`: Release resources
- **Threading**: Coroutine-based async operations
- **Error Handling**: Try-catch with logging

#### PreferencesManager
- **Responsibility**: Manage app preferences using SharedPreferences
- **Key Methods**:
  - `isAccessibilityServiceEnabled()`: Get accessibility status
  - `isOverlayEnabled()`: Get overlay status
  - `getGestureSensitivity()`: Get sensitivity setting
  - `isVibrationFeedbackEnabled()`: Get vibration setting
- **Storage**: SharedPreferences (MODE_PRIVATE)
- **Persistence**: Automatic on app restart

#### PermissionManager
- **Responsibility**: Handle runtime permissions and special permissions
- **Key Methods**:
  - `isAccessibilityServiceEnabled()`: Check accessibility permission
  - `hasOverlayPermission()`: Check overlay permission
  - `hasStoragePermission()`: Check storage permission
  - `getMissingPermissions()`: Get list of missing permissions
- **Special Permissions**: Accessibility, Overlay, Screen Capture
- **Error Handling**: Graceful fallbacks for permission checks

#### BatteryOptimizer
- **Responsibility**: Battery-aware service management
- **Key Methods**:
  - `getBatteryLevel()`: Get current battery percentage
  - `isLowBattery()`: Check if in low battery mode
  - `isCharging()`: Check if device is charging
  - `shouldRunServices()`: Determine if services should run
- **Optimization**: Adjusts service frequency based on battery state

## Data Flow

### Volume Control Flow
```
User taps Quick Settings Tile
        ↓
VolumeQuickSettingsTile.onClick()
        ↓
AudioManager.setStreamVolume()
        ↓
System displays volume UI
        ↓
MainViewModel.updateVolumeLevel()
        ↓
LiveData notifies UI
        ↓
MainActivity updates display
```

### Screenshot Capture Flow
```
User performs 3-finger swipe
        ↓
GestureAccessibilityService.onGesture()
        ↓
provideHapticFeedback()
        ↓
startScreenshotService()
        ↓
ScreenshotBackgroundService.onStartCommand()
        ↓
ScreenshotManager.captureScreenshot()
        ↓
MediaProjection captures screen
        ↓
Bitmap saved to Gallery
        ↓
Notification shows success/failure
```

### Permission Request Flow
```
App Launch
        ↓
PermissionManager checks permissions
        ↓
MainActivity displays status
        ↓
User taps "Enable Accessibility Service"
        ↓
PermissionManager.openAccessibilitySettings()
        ↓
System Settings opens
        ↓
User enables service
        ↓
MainActivity.onResume() refreshes status
        ↓
UI updates to show enabled state
```

## Threading Model

### Main Thread
- UI updates
- User interactions
- Activity/Fragment lifecycle

### Coroutine Dispatcher
- **Dispatchers.Main**: UI updates
- **Dispatchers.Default**: CPU-intensive work
- **Dispatchers.IO**: File I/O, network

### Background Services
- Accessibility Service: System thread
- Screenshot Service: Coroutine-based
- Overlay Service: Main thread (UI updates)

## State Management

### LiveData
Used for observable state in ViewModel:
```kotlin
private val _volumeLevel = MutableLiveData<Int>()
val volumeLevel: LiveData<Int> = _volumeLevel

// Update from service
_volumeLevel.value = newVolume

// Observe in UI
viewModel.volumeLevel.observe(this) { volume ->
    updateVolumeDisplay(volume)
}
```

### SharedPreferences
Used for persistent settings:
```kotlin
// Save
sharedPreferences.edit().putBoolean(KEY, value).apply()

// Load
val value = sharedPreferences.getBoolean(KEY, default)
```

## Error Handling

### Try-Catch Blocks
Used for operations that may fail:
```kotlin
try {
    // Operation that may fail
} catch (e: Exception) {
    Timber.e(e, "Error message")
    // Handle error gracefully
}
```

### Logging
Comprehensive logging with Timber:
```kotlin
Timber.d("Debug message")      // Debug
Timber.i("Info message")       // Info
Timber.w("Warning message")    // Warning
Timber.e(e, "Error message")   // Error
```

## Performance Considerations

### Memory Optimization
1. Bitmap recycling after use
2. ImageReader cleanup
3. Service resource cleanup
4. Coroutine cancellation

### CPU Optimization
1. Event-based gesture detection (no polling)
2. Efficient permission checking
3. Lazy initialization of managers
4. Service sleep when idle

### Battery Optimization
1. Foreground service only when needed
2. Gesture debouncing (500ms)
3. Battery-aware service frequency
4. Proper resource cleanup

## Security Considerations

### Permissions
- All dangerous permissions properly declared
- Runtime permission requests for Android 6.0+
- Special permission handling for accessibility and overlay

### Data Storage
- SharedPreferences with MODE_PRIVATE
- No sensitive data in logs
- Proper file permissions for screenshots

### Service Security
- Services protected with appropriate permissions
- Input validation for all user data
- Proper intent filtering

## Testing Strategy

### Unit Tests
- ViewModel logic
- Manager methods
- Utility functions

### Integration Tests
- Service interactions
- Permission flows
- Screenshot capture

### Manual Testing
- Permission dialogs
- Gesture detection
- Screenshot saving
- Overlay functionality

## Extensibility

### Adding New Features
1. Create new Service/Manager as needed
2. Update ViewModel if state management needed
3. Add UI in MainActivity or new Activity
4. Update permissions in AndroidManifest.xml

### Example: Adding New Gesture
```kotlin
// 1. Add to GestureAccessibilityService
override fun onGesture(gestureId: Int): Boolean {
    when (gestureId) {
        NEW_GESTURE_ID -> handleNewGesture()
    }
}

// 2. Implement handler
private fun handleNewGesture() {
    // Implementation
}

// 3. Update settings if needed
// 4. Add UI toggle in MainActivity
```

## Dependency Injection

Currently uses manual dependency injection:
```kotlin
val permissionManager = PermissionManager(context)
val preferencesManager = PreferencesManager(context)
```

Could be enhanced with Hilt or Dagger2 for larger projects.

## Version Compatibility

### API Level Handling
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    // Android 10+ specific code
} else {
    // Fallback for older versions
}
```

### Feature Detection
```kotlin
if (mediaProjectionManager != null) {
    // Use MediaProjection
}
```

## Future Architecture Improvements

1. **Dependency Injection**: Implement Hilt for cleaner DI
2. **Repository Pattern**: Add data layer abstraction
3. **Use Cases**: Extract business logic into use cases
4. **Event Bus**: Consider EventBus for inter-component communication
5. **Database**: Add Room for complex data storage
6. **Network**: Add Retrofit for potential cloud features

---

**Last Updated**: 2024
**Architecture Version**: 1.0
