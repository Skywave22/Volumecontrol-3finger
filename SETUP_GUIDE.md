# Setup & Build Guide

## System Requirements

### Minimum Requirements
- **OS**: Windows 10+, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **RAM**: 8 GB (16 GB recommended)
- **Disk Space**: 10 GB free space
- **Java**: JDK 11 or higher

### Android Development Tools
- **Android Studio**: Latest version (Flamingo or newer)
- **Android SDK**: API 34
- **Build Tools**: 34.0.0 or higher
- **Gradle**: 8.1.0 (included with Android Studio)

## Installation Steps

### 1. Install Android Studio

#### Windows
1. Download from [developer.android.com](https://developer.android.com/studio)
2. Run the installer
3. Follow the setup wizard
4. Install Android SDK and Build Tools when prompted

#### macOS
1. Download from [developer.android.com](https://developer.android.com/studio)
2. Drag Android Studio to Applications folder
3. Open Android Studio
4. Complete the setup wizard

#### Linux (Ubuntu)
```bash
# Install Java
sudo apt-get update
sudo apt-get install openjdk-11-jdk

# Download Android Studio
wget https://developer.android.com/studio/install

# Extract and run
unzip android-studio-*.zip
cd android-studio/bin
./studio.sh
```

### 2. Install Android SDK

1. Open Android Studio
2. Go to **Tools → SDK Manager**
3. Install the following:
   - **SDK Platforms**: Android 14 (API 34)
   - **SDK Tools**: 
     - Android SDK Build-Tools 34.0.0
     - Android Emulator
     - Android SDK Platform-Tools
     - Android SDK Tools

### 3. Clone/Extract Project

```bash
# Option 1: If you have the project as a ZIP
unzip android-native-project.zip
cd android-native-project

# Option 2: If you have it as a git repository
git clone <repository-url>
cd android-native-project
```

### 4. Open Project in Android Studio

1. Open Android Studio
2. Click **File → Open**
3. Navigate to the project directory
4. Click **OK**
5. Wait for Gradle sync to complete

### 5. Configure Gradle (if needed)

Edit `gradle.properties`:
```properties
# Increase heap size if you have RAM
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m

# Enable parallel builds
org.gradle.parallel=true

# Enable daemon
org.gradle.daemon=true
```

## Building the Project

### Method 1: Using Android Studio

1. **Build APK**
   - Click **Build → Build Bundle(s) / APK(s) → Build APK(s)**
   - Wait for the build to complete
   - APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

2. **Build Release APK**
   - Click **Build → Build Bundle(s) / APK(s) → Build Bundle(s)**
   - Or use **Build → Generate Signed Bundle / APK**

### Method 2: Using Command Line

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (requires signing key)
./gradlew assembleRelease

# Build and install on connected device
./gradlew installDebug

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

### Method 3: Using Gradle Wrapper (Recommended)

```bash
# On Windows
gradlew.bat assembleDebug

# On macOS/Linux
./gradlew assembleDebug
```

## Running on Device/Emulator

### Option 1: Using Android Studio

1. **Connect Device**
   - Enable USB Debugging on your Android phone
   - Connect via USB cable
   - Click **Run → Run 'app'**

2. **Use Emulator**
   - Click **AVD Manager**
   - Create a new virtual device (API 34 recommended)
   - Start the emulator
   - Click **Run → Run 'app'**

### Option 2: Using Command Line

```bash
# List connected devices
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Run app
adb shell am start -n com.example.volumescreenshot/.ui.MainActivity

# View logs
adb logcat | grep "VolumeScreenshot"
```

## First Launch Setup

### Step 1: Grant Permissions

When you launch the app for the first time:

1. **Accessibility Service**
   - Tap "Enable Accessibility Service"
   - Go to Settings → Accessibility
   - Find "Volume & Screenshot Service"
   - Toggle it ON
   - Confirm the permission dialog

2. **Overlay Permission** (if using overlay)
   - Tap "Enable Overlay" in the app
   - Go to Settings → Apps → Special app access → Display over other apps
   - Find the app and toggle it ON

3. **Storage Permission**
   - Allow when prompted
   - Or go to Settings → Apps → Permissions → Storage

### Step 2: Add Quick Settings Tile

1. Swipe down twice to open Quick Settings
2. Tap the edit button (pencil icon)
3. Scroll down to find "Volume Control"
4. Drag it to your Quick Settings
5. Tap the tile to test

### Step 3: Test Features

1. **Test Quick Settings**
   - Tap the Volume Control tile
   - Volume should increase/cycle

2. **Test 3-Finger Swipe**
   - Perform a 3-finger swipe down on any screen
   - Screenshot should be saved to Gallery

3. **Test Overlay** (if enabled)
   - Floating widget should appear
   - Test volume buttons and slider

## Troubleshooting Build Issues

### Gradle Sync Failed

**Error**: "Failed to sync Gradle"

**Solution**:
```bash
# Clean and rebuild
./gradlew clean
./gradlew build

# Or in Android Studio:
# File → Invalidate Caches / Restart → Invalidate and Restart
```

### Compilation Errors

**Error**: "Unresolved reference to 'Timber'"

**Solution**: Ensure dependencies are installed
```bash
./gradlew build --refresh-dependencies
```

### SDK Not Found

**Error**: "Android SDK not found"

**Solution**:
1. Go to **File → Project Structure**
2. Set Android SDK location
3. Or set `ANDROID_HOME` environment variable:
   ```bash
   export ANDROID_HOME=~/Android/Sdk  # macOS/Linux
   setx ANDROID_HOME %USERPROFILE%\AppData\Local\Android\Sdk  # Windows
   ```

### Out of Memory Error

**Error**: "OutOfMemoryError: Java heap space"

**Solution**: Increase heap size in `gradle.properties`
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m
```

## Troubleshooting Runtime Issues

### App Crashes on Launch

**Check**: 
1. Look at logcat for error messages
2. Verify all permissions are granted
3. Check if Accessibility Service is enabled

**Solution**:
```bash
# View crash logs
adb logcat | grep "FATAL\|Exception\|Error"

# Clear app data and reinstall
adb uninstall com.example.volumescreenshot
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Accessibility Service Not Detecting Gestures

**Check**:
1. Verify service is enabled in Settings
2. Check if service name matches in manifest
3. Try restarting the device

**Solution**:
```bash
# Check if service is enabled
adb shell settings get secure enabled_accessibility_services

# Verify service is running
adb shell dumpsys accessibility
```

### Screenshots Not Saving

**Check**:
1. Verify storage permission is granted
2. Check if Pictures/Screenshots folder exists
3. Verify device has free storage space

**Solution**:
```bash
# Check storage permissions
adb shell pm dump com.example.volumescreenshot | grep -A 5 "permissions"

# Create screenshots directory
adb shell mkdir -p /sdcard/Pictures/Screenshots
```

### Overlay Not Displaying

**Check**:
1. Verify overlay permission is granted
2. Check if overlay is enabled in app settings
3. Verify SYSTEM_ALERT_WINDOW permission in manifest

**Solution**:
```bash
# Check overlay permission
adb shell settings get secure overlay_permission_list

# Restart overlay service
adb shell am startservice com.example.volumescreenshot/.service.FloatingOverlayService
```

## Development Tips

### Enable Verbose Logging

Edit `MainActivity.kt`:
```kotlin
if (BuildConfig.DEBUG) {
    Timber.plant(Timber.DebugTree())
}
```

### View Real-Time Logs

```bash
# Filter logs by tag
adb logcat -s "VolumeScreenshot"

# Filter by log level
adb logcat *:E  # Only errors
adb logcat *:W  # Warnings and errors
adb logcat *:D  # Debug and above
```

### Debug Services

```bash
# Check accessibility services
adb shell dumpsys accessibility

# Check running services
adb shell dumpsys activity services

# Check permissions
adb shell pm list permissions -d
```

### Performance Profiling

1. Open Android Studio
2. Go to **View → Tool Windows → Profiler**
3. Select your running app
4. Monitor CPU, Memory, and Battery usage

## Release Build

### Create Signing Key

```bash
# Generate keystore
keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000

# Or use Android Studio:
# Build → Generate Signed Bundle / APK
```

### Build Release APK

```bash
# Using command line
./gradlew assembleRelease -Pandroid.injected.signing.store.file=release.keystore \
  -Pandroid.injected.signing.store.password=<password> \
  -Pandroid.injected.signing.key.alias=<alias> \
  -Pandroid.injected.signing.key.password=<password>

# Or use Android Studio UI
# Build → Generate Signed Bundle / APK
```

## Continuous Integration

### GitHub Actions Example

Create `.github/workflows/build.yml`:
```yaml
name: Build

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Build with Gradle
        run: ./gradlew build
```

## Next Steps

1. **Explore the Code**: Read through the source files
2. **Read Architecture**: Check `ARCHITECTURE.md`
3. **Test Features**: Follow the First Launch Setup
4. **Customize**: Modify colors, strings, and features as needed
5. **Deploy**: Build release APK and distribute

## Additional Resources

- [Android Developer Documentation](https://developer.android.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Material Design Guidelines](https://material.io/design)

## Support

If you encounter issues:

1. Check the **Troubleshooting** section above
2. Review logcat output: `adb logcat`
3. Check Android Studio's **Build → Analyze APK**
4. Verify all permissions in `AndroidManifest.xml`

---

**Last Updated**: 2024
**Android Studio Version**: Flamingo or newer
**Gradle Version**: 8.1.0
