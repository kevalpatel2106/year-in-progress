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
-keepattributes SourceFile,LineNumberTable

# Setting the build versions for dead code elemination.
# https://jakewharton.com/digging-into-d8-and-r8/
-assumevalues class android.os.Build$VERSION {
    int SDK_INT return 21..2147483647;
}
-assumevalues class com.kevalpatel2106.yip.BuildConfig {
    boolean DEBUG return false;
}
-assumevalues class com.kevalpatel2106.yip.*.BuildConfig {
    boolean DEBUG return false;
}

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# Proguard configuration for removing logs
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}
-assumenosideeffects class java.io.PrintStream {
     public void println(%);
     public void println(**);
 }

# Guava ProGuard (Sugar ORM depends on Guava)
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# Dagger depends on ErrorProne, but they are not used in runtime; Ignore
-dontwarn com.google.errorprone.annotations.*
