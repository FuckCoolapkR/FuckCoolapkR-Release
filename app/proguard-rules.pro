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

-keep class * implements de.robv.android.xposed.IXposedHookLoadPackage {
    public void *(de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam);
}

-keep class * implements de.robv.android.xposed.IXposedHookInitPackageResources {
    public void *(de.robv.android.xposed.callbacks.XC_InitPackageResources$InitPackageResourcesParam);
}

-keep class * implements de.robv.android.xposed.IXposedHookZygoteInit {
    public void *(de.robv.android.xposed.IXposedHookZygoteInit$StartupParam);
}

-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void check*(...);
    public static void throw*(...);
}

-assumenosideeffects class java.util.Objects {
    public static java.lang.Object requireNonNull(java.lang.Object, java.lang.String);
}

-keep class com.github.megatronking.stringfog.Base64

-keep class org.fuck.coolapk.XposedEntry {
    <init>();
}

-keep class org.fuck.coolapk.CoolapkUtils {
    int a;
    getFd();
    readLink(int);
}

-keepnames class com.microsoft.appcenter.**

-keepnames class org.springframework.security.crypto.**

-keepnames class kotlin.**

-keep class org.luckypray.dexkit.** { *; }

# 方法名等混淆指定配置
-obfuscationdictionary fuckcoolapkr-dic.txt
# 类名混淆指定配置
-classobfuscationdictionary fuckcoolapkr-dic.txt
# 包名混淆指定配置
-packageobfuscationdictionary fuckcoolapkr-dic.txt

-repackageclasses "org.fuck.coolapk"
