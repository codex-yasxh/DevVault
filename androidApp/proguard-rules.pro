# DevVault ProGuard Rules — Play Store release build

# ---- kotlinx.serialization ----
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.aditya.devvault.**$$serializer { *; }
-keepclassmembers class com.aditya.devvault.** {
    *** Companion;
}
-keepclasseswithmembers class com.aditya.devvault.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ---- Ktor ----
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
-keep class kotlinx.coroutines.** { *; }

# ---- SQLDelight ----
-keep class app.cash.sqldelight.** { *; }
-dontwarn app.cash.sqldelight.**

# ---- Koin ----
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# ---- AndroidX / Compose ----
-keep class androidx.compose.** { *; }
-keep class androidx.activity.** { *; }
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

# ---- Kotlin Coroutines ----
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
