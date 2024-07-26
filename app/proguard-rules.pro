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

# Keep rules for BouncyCastle, Conscrypt, OpenJSSE, and internal Java classes
-keep class org.bouncycastle.** { *; }
-keep class org.conscrypt.** { *; }
-keep class org.openjsse.** { *; }

-keep class javax.naming.** { *; }
-keep class sun.security.** { *; }
-keep class sun.misc.** { *; }
-keep class sun.net.** { *; }
-keep class sun.net.www.** { *; }
-keep class sun.util.logging.** { *; }

-keep class com.android.org.conscrypt.** { *; }
-keep class org.apache.harmony.xnet.provider.jsse.** { *; }

# For SSL/TLS-related classes that might be required
-keep class javax.net.ssl.** { *; }
-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }
