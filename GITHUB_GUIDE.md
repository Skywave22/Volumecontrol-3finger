# GitHub Setup & Automated Build Guide

This guide shows you how to upload the Android project to GitHub and set up automated APK builds using GitHub Actions.

## Step 1: Create a GitHub Account

1. Go to [github.com](https://github.com)
2. Click **Sign up**
3. Follow the registration process
4. Verify your email

## Step 2: Create a New Repository

### Method 1: Using GitHub Web Interface

1. Log in to GitHub
2. Click the **+** icon in the top-right corner
3. Select **New repository**
4. Fill in the details:
   - **Repository name**: `android-volume-screenshot` (or your preferred name)
   - **Description**: "Android utility app with Quick Settings volume tile and 3-finger swipe screenshot"
   - **Visibility**: Choose **Public** (for open source) or **Private** (for personal use)
   - **Initialize with**: Leave unchecked (we'll push existing code)
5. Click **Create repository**

### Method 2: Using Git Command Line

```bash
# Create a new directory for your project
mkdir android-volume-screenshot
cd android-volume-screenshot

# Initialize git
git init

# Add GitHub as remote
git remote add origin https://github.com/YOUR_USERNAME/android-volume-screenshot.git

# Create initial commit
git add .
git commit -m "Initial commit: Android Volume & Screenshot utility"

# Push to GitHub
git branch -M main
git push -u origin main
```

## Step 3: Upload Project to GitHub

### Option A: Using Git Command Line (Recommended)

```bash
# Navigate to the project directory
cd /home/ubuntu/android-native-project

# Initialize git repository
git init

# Add all files
git add .

# Create initial commit
git commit -m "Initial commit: Complete Android Volume & Screenshot app with MVVM architecture"

# Add GitHub remote (replace YOUR_USERNAME and REPO_NAME)
git remote add origin https://github.com/YOUR_USERNAME/android-volume-screenshot.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### Option B: Using GitHub Desktop

1. Download [GitHub Desktop](https://desktop.github.com)
2. Sign in with your GitHub account
3. Click **File → Clone Repository**
4. Select your newly created repository
5. Click **Clone**
6. Copy the project files into the cloned directory
7. Commit and push changes

### Option C: Using Web Upload

1. Go to your GitHub repository
2. Click **Add file → Upload files**
3. Drag and drop the project files
4. Write a commit message
5. Click **Commit changes**

## Step 4: Verify GitHub Actions Workflow

The project already includes a GitHub Actions workflow file (`.github/workflows/build.yml`) that will:

1. **Automatically trigger** when you push code
2. **Build the APK** using Gradle
3. **Upload artifacts** (APK files) for download
4. **Create releases** when you tag a version

### Check Workflow Status:

1. Go to your GitHub repository
2. Click the **Actions** tab
3. You should see a workflow run in progress or completed
4. Click on the workflow to see build details

## Step 5: Download Built APK

### From Workflow Artifacts:

1. Go to **Actions** tab
2. Click the latest workflow run
3. Scroll down to **Artifacts**
4. Download `app-debug.apk` or `app-release-unsigned.apk`

### From Releases:

1. Go to **Releases** tab
2. Click on a release
3. Download the APK files

## Step 6: Create a Release with Signed APK

### Generate Signing Key:

```bash
# Create a keystore file
keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias release-key

# You'll be prompted for:
# - Password (remember this!)
# - First and last name
# - Organization unit
# - Organization name
# - City
# - State
# - Country code
```

### Add Signing Configuration:

Edit `app/build.gradle.kts`:

```kotlin
android {
    // ... existing config ...
    
    signingConfigs {
        create("release") {
            storeFile = file("../release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "your_password"
            keyAlias = "release-key"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "your_password"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### Store Keystore Securely on GitHub:

1. **DO NOT** commit `release.keystore` to GitHub
2. Add to `.gitignore`:
   ```
   release.keystore
   *.keystore
   ```
3. Store the keystore file securely (locally only)

### Set GitHub Secrets:

1. Go to repository **Settings**
2. Click **Secrets and variables → Actions**
3. Click **New repository secret**
4. Add secrets:
   - Name: `KEYSTORE_PASSWORD` → Value: your keystore password
   - Name: `KEY_PASSWORD` → Value: your key password

### Update Workflow for Signed Builds:

Edit `.github/workflows/build.yml`:

```yaml
- name: Build Release APK (signed)
  env:
    KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
    KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
  run: ./gradlew assembleRelease
```

## Step 7: Create Tags and Releases

### Create a Version Tag:

```bash
# Tag a version
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push tag to GitHub
git push origin v1.0.0
```

### GitHub will automatically:

1. Trigger a build workflow
2. Create a release page
3. Upload APK artifacts

## Step 8: Continuous Integration Best Practices

### Update Code and Push:

```bash
# Make changes to your code
# ... edit files ...

# Stage changes
git add .

# Commit with a meaningful message
git commit -m "Add new feature: custom gesture patterns"

# Push to GitHub
git push origin main
```

### GitHub Actions will:

1. Automatically detect the push
2. Start a build workflow
3. Compile the APK
4. Upload artifacts
5. Notify you of build status

## Common GitHub Commands

```bash
# Clone a repository
git clone https://github.com/YOUR_USERNAME/android-volume-screenshot.git

# Check status
git status

# Add files
git add .
git add filename.kt

# Commit changes
git commit -m "Your commit message"

# Push to GitHub
git push origin main

# Pull latest changes
git pull origin main

# Create a new branch
git checkout -b feature/new-feature

# Switch branches
git checkout main

# Merge branch
git merge feature/new-feature

# View commit history
git log

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes)
git reset --hard HEAD~1
```

## Troubleshooting

### Build Failed on GitHub

1. Go to **Actions** tab
2. Click the failed workflow
3. Expand the failed step to see error details
4. Common issues:
   - Missing dependencies: Run `./gradlew build` locally first
   - Gradle version mismatch: Update `gradle/wrapper/gradle-wrapper.properties`
   - Java version: Ensure JDK 11 is used

### APK Not Downloading

1. Check if workflow completed successfully
2. Go to **Actions → Latest workflow → Artifacts**
3. If no artifacts, check build logs for errors
4. Ensure `assembleDebug` task ran successfully

### Authentication Issues

```bash
# Set up Git credentials
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Or use personal access token
git remote set-url origin https://YOUR_TOKEN@github.com/YOUR_USERNAME/android-volume-screenshot.git
```

## GitHub Repository Structure

After setup, your repository will look like:

```
android-volume-screenshot/
├── .github/
│   └── workflows/
│       └── build.yml              # Automated build workflow
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/...          # Kotlin source files
│   │   │   └── res/...           # Resources
│   │   ├── test/
│   │   └── androidTest/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── .gitignore
├── README.md
├── SETUP_GUIDE.md
├── ARCHITECTURE.md
├── FEATURES.md
└── PROJECT_SUMMARY.txt
```

## Next Steps

1. **Create GitHub account** if you don't have one
2. **Create a new repository** on GitHub
3. **Push the project** using Git
4. **Verify GitHub Actions** workflow runs successfully
5. **Download APK** from artifacts
6. **Test the APK** on your device
7. **Create releases** with version tags

## Additional Resources

- [GitHub Docs](https://docs.github.com)
- [Git Documentation](https://git-scm.com/doc)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Android Build Documentation](https://developer.android.com/build)

## Support

If you encounter issues:

1. Check GitHub Actions logs for build errors
2. Review the troubleshooting section above
3. Verify all files are committed correctly
4. Ensure `.gitignore` is properly configured

---

**Happy coding! 🚀**
