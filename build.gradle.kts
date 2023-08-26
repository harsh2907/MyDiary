// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    dependencies{
//        classpath("com.android.tools.build:gradle:8.1.0")
//
//    }
//}
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version("2.47") apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
    id("io.realm.kotlin") version "1.0.2" apply false
}