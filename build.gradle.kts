buildscript {
    dependencies {
        classpath(libs.gradle)
        classpath(libs.google.services)
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.5")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id ("com.google.dagger.hilt.android") version "2.48.1" apply false
}