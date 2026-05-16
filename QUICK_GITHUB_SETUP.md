# Quick GitHub Setup (5 Minutes)

## Step 1: Create GitHub Account (if you don't have one)
- Go to https://github.com/signup
- Sign up with your email
- Verify your email

## Step 2: Create New Repository on GitHub

1. Go to https://github.com/new
2. Fill in:
   - **Repository name**: `android-volume-screenshot`
   - **Description**: `Android utility app with Quick Settings volume tile and 3-finger swipe screenshot`
   - **Visibility**: Choose **Public** or **Private**
3. Click **Create repository**

## Step 3: Upload Project to GitHub

### On Windows/Mac/Linux:

```bash
# Navigate to the project directory
cd /path/to/android-native-project

# Initialize git
git init

# Add all files
git add .

# Create first commit
git commit -m "Initial commit: Android Volume & Screenshot app"

# Add GitHub remote (replace YOUR_USERNAME and REPO_NAME)
git remote add origin https://github.com/YOUR_USERNAME/android-volume-screenshot.git

# Push to GitHub
git branch -M main
git push -u origin main
```

## Step 4: Verify Automated Build

1. Go to your GitHub repository
2. Click the **Actions** tab
3. You should see a workflow running
4. Wait for it to complete (usually 5-10 minutes)
5. Once complete, download the APK from **Artifacts**

## Step 5: Download Your APK

### From GitHub Actions:

1. Go to **Actions** tab
2. Click the latest workflow run
3. Scroll down to **Artifacts**
4. Download `app-debug.apk`

### Install on Your Phone:

```bash
adb install app-debug.apk
```

## That's It! 🎉

Your Android app is now:
- ✅ Uploaded to GitHub
- ✅ Automatically building on every push
- ✅ Generating APK files automatically
- ✅ Ready to download and test

## Next Time You Update Code:

```bash
# Make changes to your code
# ...

# Commit and push
git add .
git commit -m "Your change description"
git push origin main

# GitHub Actions will automatically build a new APK!
```

## Troubleshooting

**"fatal: not a git repository"**
```bash
git init
```

**"fatal: The current branch main has no upstream branch"**
```bash
git push -u origin main
```

**"Permission denied (publickey)"**
- Set up SSH key: https://docs.github.com/en/authentication/connecting-to-github-with-ssh
- Or use HTTPS with personal access token

**Build failed on GitHub**
- Check the Actions tab for error logs
- Usually due to missing dependencies or Java version mismatch

---

For detailed instructions, see **GITHUB_GUIDE.md**
