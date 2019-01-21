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
-dontskipnonpubliclibraryclasses
-forceprocessing
-optimizationpasses 5
-verbose
-ignorewarnings

# Keep name of all the classes
#-keepnames class ** { *; } #Remove if you don't have to stake trace

#Keep anotations there
-keepattributes *Annotation*

##---------------Begin: proguard configuration for removing logs  ----------
#remove log class
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}
# Remove sout
-assumenosideeffects class java.io.PrintStream {
     public void println(%);
     public void println(**);
 }
##---------------End: proguard configuration for removing logs  ----------

##---------------Begin: proguard configuration for Crashlytics 2.+  ----------
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile, LineNumberTable, *Annotation*

# If you are using custom exceptions, add this line so that custom exception types are skipped during obfuscation:
-keep public class * extends java.lang.Exception

# For Fabric to properly de-obfuscate your crash reports, you need to remove this line from your ProGuard config:
# -printmapping mapping.txt
##---------------End: proguard configuration for Crashlytics 2.+  ----------