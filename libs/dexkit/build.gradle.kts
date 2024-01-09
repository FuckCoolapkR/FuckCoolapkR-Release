@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "org.luckypray.dexkit"
    compileSdk = 33
    sourceSets {
        val main by getting
        main.apply {
            manifest.srcFile("AndroidManifest.xml")
            java.setSrcDirs(listOf("DexKit/dexkit/src/main/java"))
            res.setSrcDirs(listOf("DexKit/dexkit/src/main/res"))
        }
    }
}

dependencies {
    implementation("com.google.flatbuffers:flatbuffers-java:23.5.26")
}
