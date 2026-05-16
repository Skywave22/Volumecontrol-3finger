# ProGuard rules for VolumeScreenshot app

# Keep all classes in our package
-keep class com.example.volumescreenshot.** { *; }

# Keep Android framework classes
-keep class android.** { *; }
-keep interface android.** { *; }

# Keep androidx classes
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Keep Kotlin metadata
-keepclassmembers class ** {
    *** Companion;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep R classes
-keepclassmembers class **.R$* {
    public static <fields>;
}
