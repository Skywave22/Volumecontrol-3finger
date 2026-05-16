# Volume & Screenshot Utility - Android App

A feature-rich Android utility app built with Kotlin and MVVM architecture. Provides Quick Settings volume control, 3-finger swipe screenshot capture, and a floating overlay UI.

## Features

### 1. Quick Settings Volume Tile
- **Quick Settings Integration**: Add a tile to the notification shade for instant volume control
- **One-Tap Volume Cycling**: Tap to cycle through volume levels
- **Visual Feedback**: Shows current volume level and status
- **Standard Android UI**: Displays the native volume UI when adjusting
- **Android 6.0+ Support**: Works on API 24 and above

### 2. 3-Finger Swipe Screenshot
- **Accessibility Service**: Detects 3-finger swipe down gesture anywhere on screen
- **MediaProjection API**: Captures screen using system APIs
- **Gallery Integration**: Saves screenshots to device's Pictures/Screenshots folder
- **Haptic Feedback**: Optional vibration confirmation when screenshot is taken
- **Battery Optimized**: Efficient resource usage with proper cleanup

### 3. Floating Overlay UI
- **Always-On Control**: Floating volume control widget
- **Draggable Interface**: Move overlay around the screen
- **Volume Slider**: Precise volume adjustment
- **Quick Buttons**: Volume up/down buttons
- **Customizable**: Enable/disable from settings

### 4. Settings & Customization
- **Gesture Sensitivity**: Adjust 3-finger swipe detection sensitivity
- **Vibration Feedback**: Toggle haptic feedback on/off
- **Permission Management**: User-friendly permission request dialogs
- **Status Display**: Real-time permission and service status

### 5. Background Service
- **Foreground Service**: Runs continuously without being killed
- **Battery Optimization**: Intelligent power management
- **Android 12+ Compatible**: Proper foreground service handling
- **Graceful Cleanup**: Proper resource management

## Project Structure

```
android-native-project/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/volumescreenshot/
│   │   │   │   ├── service/
│   │   │   │   │   ├── VolumeQuickSettingsTile.kt          # Quick Settings tile
│   │   │   │   │   ├── GestureAccessibilityService.kt      # Gesture detection
│   │   │   │   │   ├── ScreenshotBackgroundService.kt      # Screenshot service
│   │   │   │   │   └── FloatingOverlayService.kt           # Overlay UI
│   │   │   │   ├── ui/
│   │   │   │   │   ├── MainActivity.kt                     # Main activity
│   │   │   │   │   └── SettingsActivity.kt                 # Settings screen
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── MainViewModel.kt                    # MVVM ViewModel
│   │   │   │   └── util/
│   │   │   │       ├── ScreenshotManager.kt                # Screenshot capture
│   │   │   │       ├── PreferencesManager.kt               # Settings storage
│   │   │   │       ├── PermissionManager.kt                # Permission handling
│   │   │   │       └── BatteryOptimizer.kt                 # Battery optimization
│   │   │   ├── res/
│   │   │   │   ├── layout/                                 # UI layouts
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml                         # String resources
│   │   │   │   │   ├── colors.xml                          # Color definitions
│   │   │   │   │   └── themes.xml                          # App theme
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── ic_volume.xml                       # Volume icon
│   │   │   │   │   └── ic_screenshot.xml                   # Screenshot icon
│   │   │   │   └── xml/
│   │   │   │       ├── accessibility_service_config.xml    # Accessibility config
│   │   │   │       ├── data_extraction_rules.xml           # Data security
│   │   │   │       └── backup_rules.xml                    # Backup config
│   │   │   └── AndroidManifest.xml                         # App manifest
│   │   ├── androidTest/                                    # Instrumented tests
│   │   └── test/                                           # Unit tests
│   ├── build.gradle.kts                                    # App build config
│   └── proguard-rules.pro                                  # ProGuard rules
├── build.gradle.kts                                        # Root build config
├── settings.gradle.kts                                     # Gradle settings
├── gradle.properties                                       # Gradle properties
└── README.md                                               # This file
```

## Required Permissions

The app requires the following permissions to function:

| Permission | Purpose | Android Version |
|-----------|---------|-----------------|
| `BIND_ACCESSIBILITY_SERVICE` | Detect 3-finger swipe gestures | 4.0+ |
| `SYSTEM_ALERT_WINDOW` | Display floating overlay | 4.0+ |
| `WRITE_EXTERNAL_STORAGE` | Save screenshots to gallery | 4.0+ |
| `READ_EXTERNAL_STORAGE` | Access gallery for screenshots | 4.0+ |
| `VIBRATE` | Haptic feedback on screenshot | 4.0+ |
| `FOREGROUND_SERVICE` | Run background service | 8.0+ |
| `FOREGROUND_SERVICE_MEDIA_PROJECTION` | MediaProjection service | 12.0+ |

## Installation & Setup

### Prerequisites
- Android Studio (latest version)
- Android SDK 34 or higher
- Kotlin 1.9.0 or higher
- Gradle 8.1.0 or higher

### Build Instructions

1. **Clone/Extract the Project**
   ```bash
   cd android-native-project
   ```

2. **Build the Project**
   ```bash
   ./gradlew build
   ```

3. **Run on Emulator or Device**
   ```bash
   ./gradlew installDebug
   ```

4. **Generate APK**
   ```bash
   ./gradlew assembleDebug
   ```
   The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

### First Launch Setup

1. **Grant Accessibility Service Permission**
   - Open Settings → Accessibility
   - Find "Volume & Screenshot Service"
   - Enable the service
   - Confirm the permission prompt

2. **Grant Overlay Permission** (if using overlay)
   - Open Settings → Apps → Special app access → Display over other apps
   - Enable the app

3. **Grant Storage Permission**
   - Allow access to Pictures/Screenshots folder when prompted

## Usage Guide

### Quick Settings Volume Tile

1. **Add to Quick Settings**
   - Swipe down twice to open Quick Settings
   - Tap the edit button (pencil icon)
   - Find "Volume Control" tile
   - Drag it to your Quick Settings

2. **Use the Tile**
   - Tap once to increase volume
   - Tap multiple times to cycle through levels
   - Tap to reset to 0 when at max

### 3-Finger Swipe Screenshot

1. **Enable Accessibility Service**
   - Open the app
   - Toggle "Enable Accessibility Service"
   - Grant permission in Settings

2. **Take Screenshots**
   - Perform a 3-finger swipe down gesture on any screen
   - Feel the haptic feedback (if enabled)
   - Screenshot is saved to Gallery → Pictures → Screenshots

3. **Customize Sensitivity**
   - Open Settings in the app
   - Adjust "Gesture Sensitivity" slider
   - Lower values = more sensitive, Higher values = less sensitive

### Floating Overlay UI

1. **Enable Overlay**
   - Open the app
   - Toggle "Enable Overlay"
   - Grant overlay permission if prompted

2. **Use the Overlay**
   - Floating widget appears on screen
   - Tap Volume Up/Down buttons
   - Use slider for precise control
   - Drag to move around screen
   - Tap X to close

## Architecture & Design

### MVVM Pattern
- **Model**: Data layer (PreferencesManager, ScreenshotManager)
- **View**: UI layer (MainActivity, SettingsActivity)
- **ViewModel**: Business logic layer (MainViewModel)

### Key Components

#### Services
- **VolumeQuickSettingsTile**: Quick Settings integration
- **GestureAccessibilityService**: Gesture detection
- **ScreenshotBackgroundService**: Screenshot capture
- **FloatingOverlayService**: Floating UI

#### Utilities
- **ScreenshotManager**: Handles MediaProjection and bitmap capture
- **PreferencesManager**: Manages SharedPreferences
- **PermissionManager**: Handles runtime permissions
- **BatteryOptimizer**: Battery-aware service management

#### ViewModels
- **MainViewModel**: Manages app state and business logic

### Threading Model
- **Main Thread**: UI updates and user interactions
- **Coroutines**: Async operations (screenshot capture, file I/O)
- **Background Thread**: Service operations

## Code Highlights

### Quick Settings Tile Implementation
```kotlin
override fun onClick() {
    // Cycle through volume levels
    val newVolume = if (currentVolumeLevel >= maxVolumeLevel) 0 else currentVolumeLevel + 1
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, AudioManager.FLAG_SHOW_UI)
    updateTileState()
}
```

### Gesture Detection
```kotlin
override fun onGesture(gestureId: Int): Boolean {
    if (gestureId == GESTURE_SWIPE_DOWN) {
        provideHapticFeedback()
        startScreenshotService()
        return true
    }
    return false
}
```

### Screenshot Capture
```kotlin
suspend fun captureScreenshot(callback: (Boolean, String) -> Unit) {
    val imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
    val image = imageReader.acquireLatestImage()
    val bitmap = imageToBitmap(image)
    saveBitmapToGallery(bitmap)
}
```

### Permission Checking
```kotlin
fun isAccessibilityServiceEnabled(): Boolean {
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    return enabledServices?.contains(serviceName) ?: false
}
```

## Testing

### Unit Tests
Located in `app/src/test/java/`

### Instrumented Tests
Located in `app/src/androidTest/java/`

### Manual Testing Checklist
- [ ] Quick Settings tile appears in notification shade
- [ ] Volume tile responds to taps
- [ ] Accessibility Service enables without errors
- [ ] 3-finger swipe triggers screenshot
- [ ] Screenshot appears in Gallery
- [ ] Haptic feedback works (if enabled)
- [ ] Overlay displays and is draggable
- [ ] Settings persist after app restart
- [ ] App handles low battery gracefully
- [ ] All permissions are properly requested

## Troubleshooting

### Accessibility Service Not Working
1. Check if service is enabled in Settings → Accessibility
2. Verify the service name matches in AndroidManifest.xml
3. Restart the device
4. Reinstall the app

### Screenshots Not Saving
1. Check storage permissions in Settings
2. Verify Pictures/Screenshots folder exists
3. Check device storage space
4. Look for error messages in logcat

### Overlay Not Appearing
1. Grant overlay permission in Settings
2. Check if overlay is enabled in app settings
3. Verify SYSTEM_ALERT_WINDOW permission in manifest
4. Restart the app

### Battery Drain Issues
1. Disable overlay if not needed
2. Increase gesture sensitivity to reduce false triggers
3. Disable vibration feedback
4. Check battery optimization settings

## Performance Optimization

### Battery Usage
- Foreground service runs only when needed
- Gesture detection uses efficient event filtering
- Screenshot capture cleans up resources immediately
- Overlay uses lightweight UI components

### Memory Usage
- Bitmaps are recycled after use
- ImageReader is properly closed
- Services are stopped when not needed
- Coroutines are properly cancelled

### CPU Usage
- Gesture detection uses system callbacks
- No polling or continuous scanning
- Services sleep when idle
- Efficient event handling

## Android Version Compatibility

| Feature | Min API | Target API |
|---------|---------|-----------|
| Quick Settings Tile | 24 (7.0) | 34 |
| Accessibility Service | 24 (7.0) | 34 |
| MediaProjection | 21 (5.0) | 34 |
| Overlay UI | 24 (7.0) | 34 |
| Foreground Service | 26 (8.0) | 34 |

## Security Considerations

1. **Permissions**: All dangerous permissions are properly declared and requested
2. **Data Storage**: Sensitive data stored in SharedPreferences with MODE_PRIVATE
3. **Screenshot Access**: Only app can access captured screenshots
4. **Service Security**: Services properly protected with permissions
5. **Input Validation**: All user inputs are validated before use

## Future Enhancements

- [ ] Custom gesture patterns
- [ ] Screenshot editing tools
- [ ] Cloud backup integration
- [ ] Multiple overlay widgets
- [ ] Gesture recording and playback
- [ ] Advanced battery optimization
- [ ] Widget for home screen
- [ ] Notification center integration

## Dependencies

- **AndroidX**: Core, AppCompat, Lifecycle, Preference
- **Material Design**: Material Components
- **Kotlin**: Coroutines, Standard Library
- **Logging**: Timber

## Build Configuration

- **Gradle**: 8.1.0
- **Kotlin**: 1.9.0
- **Compile SDK**: 34
- **Target SDK**: 34
- **Min SDK**: 24 (Android 7.0)

## Debugging

### Enable Verbose Logging
```kotlin
if (BuildConfig.DEBUG) {
    Timber.plant(Timber.DebugTree())
}
```

### View Logs
```bash
adb logcat | grep "VolumeScreenshot"
```

### Debug Services
```bash
adb shell dumpsys accessibility
adb shell settings get secure enabled_accessibility_services
```

## Contributing

When modifying the code:
1. Follow Kotlin style guide
2. Add comments for complex logic
3. Update documentation
4. Test on multiple Android versions
5. Check battery impact

## License

This project is provided as-is for educational and personal use.

## Support

For issues or questions:
1. Check the Troubleshooting section
2. Review logcat output
3. Verify all permissions are granted
4. Test on different Android versions

## Version History

### v1.0.0 (Initial Release)
- Quick Settings volume tile
- 3-finger swipe screenshot
- Floating overlay UI
- Settings screen
- Permission handling
- Battery optimization

---

**Built with Kotlin & MVVM Architecture**
**Supports Android 7.0 (API 24) and above**
