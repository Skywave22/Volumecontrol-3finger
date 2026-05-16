# Features Documentation

## Quick Settings Volume Tile

### Overview
A Quick Settings tile that provides instant access to volume control from the notification shade. Users can tap to increase volume or cycle through levels.

### Technical Details

**Service**: `VolumeQuickSettingsTile` (extends `TileService`)

**API Level**: Android 6.0+ (API 24+)

**Permissions**: `android.permission.BIND_QUICK_SETTINGS_TILE`

### How It Works

1. **Tile Registration**
   - Service declared in `AndroidManifest.xml` with `QS_TILE` intent filter
   - System automatically detects and registers the tile

2. **User Interaction**
   - User taps the tile in Quick Settings
   - `onClick()` is called
   - Volume is increased by 1 step
   - If at max, resets to 0

3. **Visual Feedback**
   - Tile label shows current volume: "Volume: X/Y"
   - Tile state changes based on volume level
   - Standard Android volume UI appears

### Usage

```kotlin
// Tile automatically responds to taps
override fun onClick() {
    val newVolume = if (currentVolumeLevel >= maxVolumeLevel) 0 else currentVolumeLevel + 1
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, AudioManager.FLAG_SHOW_UI)
    updateTileState()
}
```

### User Flow

```
User swipes down twice
        ↓
Quick Settings shade opens
        ↓
User finds "Volume Control" tile
        ↓
User taps tile
        ↓
Volume increases by 1 step
        ↓
Standard Android volume UI appears
        ↓
Tile label updates to show new volume
```

### Customization

**Change Volume Stream**:
```kotlin
// Currently: STREAM_MUSIC
// Options: STREAM_ALARM, STREAM_NOTIFICATION, STREAM_VOICE_CALL, etc.
audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, AudioManager.FLAG_SHOW_UI)
```

**Change Tile Behavior**:
```kotlin
// Modify onClick() to implement different logic:
// - Mute/Unmute on tap
// - Open volume settings
// - Cycle through different streams
```

---

## 3-Finger Swipe Screenshot

### Overview
Detects a 3-finger swipe down gesture anywhere on the screen and automatically captures a screenshot, saving it to the device's gallery.

### Technical Details

**Service**: `GestureAccessibilityService` (extends `AccessibilityService`)

**API Level**: Android 4.0+ (API 14+), optimized for 7.0+ (API 24+)

**Permissions**: 
- `android.permission.BIND_ACCESSIBILITY_SERVICE`
- `android.permission.CAPTURE_VIDEO_OUTPUT`
- `android.permission.WRITE_EXTERNAL_STORAGE`

### How It Works

1. **Gesture Detection**
   - Accessibility Service monitors system events
   - Detects 3-finger swipe down gesture
   - Debounces rapid repeated gestures (500ms)

2. **Haptic Feedback**
   - Vibrates to confirm gesture detection
   - Customizable in settings
   - Uses `VibrationEffect.EFFECT_TICK` on Android 10+

3. **Screenshot Capture**
   - Triggers `ScreenshotBackgroundService`
   - Uses `MediaProjection` API for screen capture
   - Converts to Bitmap and saves to file

4. **Gallery Integration**
   - Saves to `Pictures/Screenshots` folder
   - Notifies MediaStore for gallery visibility
   - Creates timestamp-based filename

### Usage

```kotlin
// Service automatically detects gestures
override fun onGesture(gestureId: Int): Boolean {
    if (gestureId == GESTURE_SWIPE_DOWN) {
        handleScreenshotGesture()
        return true
    }
    return false
}
```

### User Flow

```
User performs 3-finger swipe down
        ↓
GestureAccessibilityService detects gesture
        ↓
Haptic feedback (vibration)
        ↓
ScreenshotBackgroundService starts
        ↓
MediaProjection captures screen
        ↓
Bitmap saved to Pictures/Screenshots
        ↓
Notification shows success/failure
        ↓
User can view in Gallery app
```

### Setup Requirements

1. **Enable Accessibility Service**
   - Settings → Accessibility
   - Find "Volume & Screenshot Service"
   - Toggle ON

2. **Grant Storage Permission**
   - Allow access to Pictures folder
   - Or grant via app settings

3. **Optional: Enable Haptic Feedback**
   - Open app Settings
   - Toggle "Vibration Feedback"

### Customization

**Change Gesture Type**:
```kotlin
// Currently: GESTURE_SWIPE_DOWN
// Modify to detect other gestures:
// - GESTURE_SWIPE_UP
// - GESTURE_SWIPE_LEFT
// - GESTURE_SWIPE_RIGHT
// - GESTURE_DOUBLE_TAP
```

**Change Screenshot Format**:
```kotlin
// Currently: PNG
// Change in ScreenshotManager.saveBitmapToGallery():
bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)  // JPEG with 90% quality
```

**Change Debounce Time**:
```kotlin
// Currently: 500ms
private val gestureDebounceMs = 500L  // Modify this value
```

---

## Floating Overlay UI

### Overview
A floating widget that displays on top of other apps, providing quick access to volume control without switching apps.

### Technical Details

**Service**: `FloatingOverlayService` (extends `Service`)

**API Level**: Android 4.0+ (API 14+), optimized for 6.0+ (API 24+)

**Permissions**: `android.permission.SYSTEM_ALERT_WINDOW`

**Window Type**: 
- `TYPE_APPLICATION_OVERLAY` (Android 8.0+)
- `TYPE_PHONE` (older versions)

### How It Works

1. **Overlay Creation**
   - Creates a `LinearLayout` with volume controls
   - Adds to `WindowManager` with overlay params
   - Positioned at top-left by default

2. **Components**
   - Title: "Volume Control"
   - Volume Display: Shows current level
   - Volume Down Button: Decreases volume
   - Volume Up Button: Increases volume
   - Volume Slider: Precise control
   - Close Button: Dismisses overlay

3. **User Interaction**
   - Users can tap buttons to adjust volume
   - Slider for precise control
   - Can be moved around screen (with modification)
   - Close button stops the service

### Usage

```kotlin
// Start overlay
val intent = Intent(this, FloatingOverlayService::class.java)
intent.action = FloatingOverlayService.ACTION_START_OVERLAY
startForegroundService(intent)

// Stop overlay
val intent = Intent(this, FloatingOverlayService::class.java)
intent.action = FloatingOverlayService.ACTION_STOP_OVERLAY
startService(intent)
```

### User Flow

```
User enables overlay in app
        ↓
FloatingOverlayService starts
        ↓
Overlay widget appears on screen
        ↓
User taps volume buttons or slider
        ↓
Volume adjusts
        ↓
Standard Android volume UI appears
        ↓
User can close overlay with X button
```

### Setup Requirements

1. **Grant Overlay Permission**
   - Settings → Apps → Special app access → Display over other apps
   - Enable the app

2. **Enable in App**
   - Open app
   - Toggle "Enable Overlay"

### Customization

**Change Overlay Position**:
```kotlin
params.x = 0      // Left position
params.y = 100    // Top position
params.gravity = Gravity.TOP or Gravity.START  // Anchor point
```

**Change Overlay Size**:
```kotlin
params.width = 300  // Width in pixels
params.height = WindowManager.LayoutParams.WRAP_CONTENT
```

**Add Dragging Support**:
```kotlin
// Implement GestureDetector in FloatingOverlayService
// Handle MotionEvent to track finger position
// Update params.x and params.y on drag
```

**Change Colors**:
```kotlin
overlayContainer.setBackgroundColor(0xFF2196F3.toInt())  // Blue
// Change to other colors as needed
```

---

## Settings & Customization

### Overview
Provides a settings screen where users can configure app behavior and features.

### Features

#### 1. Gesture Sensitivity Slider
- **Range**: 0-100%
- **Default**: 50%
- **Effect**: Adjusts 3-finger swipe detection sensitivity
- **Lower values**: More sensitive (easier to trigger)
- **Higher values**: Less sensitive (harder to trigger)

#### 2. Vibration Feedback Toggle
- **Default**: Enabled
- **Effect**: Haptic feedback when screenshot is taken
- **Options**: On/Off

#### 3. Gesture Detection Status
- **Display**: Shows if gesture detection is enabled
- **Color**: Green if enabled, Orange if disabled
- **Info**: Explains how to enable

#### 4. About Section
- **Version**: App version number
- **Features**: List of main features
- **Requirements**: Minimum Android version

### User Flow

```
User opens app
        ↓
User taps "Settings" button
        ↓
SettingsActivity opens
        ↓
User adjusts sliders and toggles
        ↓
Settings are saved to SharedPreferences
        ↓
Settings persist after app restart
```

### Implementation

**Save Settings**:
```kotlin
preferencesManager.setGestureSensitivity(progress)
preferencesManager.setVibrationFeedbackEnabled(isChecked)
```

**Load Settings**:
```kotlin
val sensitivity = preferencesManager.getGestureSensitivity()
val vibrationEnabled = preferencesManager.isVibrationFeedbackEnabled()
```

---

## Permission Management

### Overview
Handles all runtime permissions and special permissions required by the app.

### Permissions Handled

| Permission | Type | Purpose | Android |
|-----------|------|---------|---------|
| BIND_ACCESSIBILITY_SERVICE | Special | Gesture detection | 4.0+ |
| SYSTEM_ALERT_WINDOW | Special | Overlay UI | 4.0+ |
| WRITE_EXTERNAL_STORAGE | Dangerous | Save screenshots | 4.0+ |
| READ_EXTERNAL_STORAGE | Dangerous | Access gallery | 4.0+ |
| VIBRATE | Normal | Haptic feedback | 4.0+ |
| FOREGROUND_SERVICE | Normal | Background service | 8.0+ |
| FOREGROUND_SERVICE_MEDIA_PROJECTION | Normal | Screenshot service | 12.0+ |

### Permission Request Flow

1. **First Launch**
   - App checks for missing permissions
   - Shows dialog with explanation
   - Offers to open Settings

2. **User Interaction**
   - User taps "Open Settings"
   - System Settings opens
   - User enables permission
   - Returns to app

3. **Status Update**
   - App refreshes permission status
   - UI updates to show enabled state
   - Features become available

### Implementation

```kotlin
// Check permission
if (permissionManager.isAccessibilityServiceEnabled()) {
    // Feature is available
}

// Request permission
permissionManager.openAccessibilitySettings()

// Get status
val missing = permissionManager.getMissingPermissions()
val status = permissionManager.getPermissionStatusMessage()
```

---

## Background Service

### Overview
Runs continuously in the background to detect gestures and capture screenshots without interruption.

### Features

1. **Foreground Service**
   - Persistent notification
   - Cannot be killed by system
   - Shows service status

2. **Battery Optimization**
   - Stops when battery critically low
   - Reduces frequency in low battery mode
   - Proper resource cleanup

3. **Lifecycle Management**
   - Starts on app launch
   - Stops when app is closed
   - Restarts on device reboot (optional)

### Implementation

```kotlin
// Start foreground service
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    startForegroundService(intent)
} else {
    startService(intent)
}

// Create notification
val notification = NotificationCompat.Builder(this, CHANNEL_ID)
    .setContentTitle("Screenshot Service")
    .setContentText("Ready to capture screenshots")
    .setSmallIcon(R.drawable.ic_volume)
    .setOngoing(true)
    .build()

startForeground(NOTIFICATION_ID, notification)
```

---

## Battery Optimization

### Overview
Intelligently manages battery usage based on device state.

### Features

1. **Battery Level Monitoring**
   - Tracks current battery percentage
   - Detects low battery mode
   - Checks charging state

2. **Service Frequency Adjustment**
   - Normal: 2 second intervals
   - Low Battery: 5 second intervals
   - Charging: 1 second intervals

3. **Critical Battery Handling**
   - Stops services below 5% battery
   - Logs battery status
   - Prevents battery drain

### Implementation

```kotlin
// Check battery status
val level = batteryOptimizer.getBatteryLevel()
val isLow = batteryOptimizer.isLowBattery()
val isCharging = batteryOptimizer.isCharging()

// Adjust service frequency
val interval = batteryOptimizer.getServiceUpdateInterval()

// Check if services should run
if (batteryOptimizer.shouldRunServices()) {
    // Run services
}
```

---

## MVVM Architecture

### Overview
Implements Model-View-ViewModel pattern for clean separation of concerns.

### Components

**Model**: Data layer
- `PreferencesManager`: Settings storage
- `ScreenshotManager`: Screenshot capture
- `PermissionManager`: Permission handling

**View**: UI layer
- `MainActivity`: Main screen
- `SettingsActivity`: Settings screen

**ViewModel**: Business logic
- `MainViewModel`: State management and business logic

### Data Binding

```kotlin
// ViewModel exposes LiveData
val volumeLevel: LiveData<Int> = _volumeLevel

// UI observes LiveData
viewModel.volumeLevel.observe(this) { volume ->
    updateVolumeDisplay(volume)
}

// ViewModel updates LiveData
_volumeLevel.value = newVolume
```

---

## Logging & Debugging

### Overview
Comprehensive logging for debugging and monitoring app behavior.

### Logging Framework

**Library**: Timber

**Log Levels**:
- `Timber.d()`: Debug messages
- `Timber.i()`: Info messages
- `Timber.w()`: Warning messages
- `Timber.e()`: Error messages

### View Logs

```bash
# All logs
adb logcat

# Filter by tag
adb logcat -s "VolumeScreenshot"

# Filter by level
adb logcat *:E  # Errors only
adb logcat *:W  # Warnings and above
```

### Example Logs

```
D/VolumeScreenshot: MainActivity created
D/VolumeScreenshot: Permission status updated: All permissions granted
D/VolumeScreenshot: Accessibility service connected
D/VolumeScreenshot: 3-finger swipe down detected!
D/VolumeScreenshot: Screenshot captured and saved
```

---

## Future Enhancement Ideas

1. **Custom Gestures**: Allow users to define custom gestures
2. **Screenshot Editing**: Built-in screenshot annotation tools
3. **Cloud Backup**: Sync screenshots to cloud storage
4. **Multiple Overlays**: Support multiple floating widgets
5. **Gesture Recording**: Record and replay gesture sequences
6. **Advanced Battery Optimization**: ML-based prediction
7. **Widget Support**: Home screen widget
8. **Notification Center Integration**: Quick actions in notifications

---

**Last Updated**: 2024
**Version**: 1.0.0
