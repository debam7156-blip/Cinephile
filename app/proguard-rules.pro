# Retrofit / Moshi: keep generated reflective adapters working under R8.
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class com.cinephile.app.BuildConfig { *; }
