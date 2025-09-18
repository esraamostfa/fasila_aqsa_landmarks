# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}
# 1) Keep generic signatures so Gson can read TypeToken<T> properly
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses,EnclosingMethod

# 2) Keep Gson internals it uses via reflection
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# 3) Keep your model classes that are deserialized via Gson
-keep class com.fasila.aqsalandmarks.model.** { *; }

# If you rely on @SerializedName / @Expose, keep those members
-keepclassmembers class com.fasila.aqsalandmarks.model.** {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.Expose <fields>;
}

# (Optional) If you have Room TypeConverters using Gson and generic lists, keep them:
-keep class **TypeConverter** { *; }
-keep class * extends com.google.gson.reflect.TypeToken { *; }

# Keep generic signatures & metadata used by reflection
-keepattributes Signature
-keepattributes InnerClasses,EnclosingMethod
-keepattributes *Annotation*

# Prevent R8 from rewriting/removing GenericTypeIndicator hierarchy
-keep class * extends com.google.firebase.database.GenericTypeIndicator

# Keep Firebase Database reflection targets
-keep class com.google.firebase.database.** { *; }

# Keep your model classes read by Firebase
-keep class com.fasila.aqsalandmarks.model.** { *; }

# (Kotlin models) keep Kotlin metadata (optional but helpful)
-keep class kotlin.Metadata { *; }
