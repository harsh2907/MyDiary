plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("io.realm.kotlin")
 //   id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mydiary"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.example.mydiary"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    //Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")

    //Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")

    // Room
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    ksp ("androidx.room:room-compiler:2.5.2")

    // Runtime
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation ("androidx.compose.runtime:runtime-livedata")

    //Splash Api
    implementation("androidx.core:core-splashscreen:1.0.1")

    //Mongo DB Realm
    implementation ("io.realm.kotlin:library-base:1.11.0")
    implementation ("io.realm.kotlin:library-sync:1.11.0")// If using Device Sync
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // If using coroutines with the SDK

//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt"){
//        version{
//            strictly("1.6.0-native-mt")
//        }
//    }

    // Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.47")
    ksp ("com.google.dagger:hilt-android-compiler:2.47")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
   // ksp ("androidx.hilt:hilt-compiler:1.0.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Coil Library
    implementation ("io.coil-kt:coil-compose:2.4.0")

    //Pager
    implementation ("com.google.accompanist:accompanist-pager:0.27.0")

    //Desugar JDK
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    //Date Time Picker
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    //Message Bar
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.5")

    //One Tap Compose
    implementation("com.github.stevdza-san:OneTapCompose:1.0.0")


//    //Google Auth
//    implementation("com.google.android.gms:play-services-auth:20.6.0")



    /*
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"
    implementation "androidx.compose.material:material-icons-extended:1.3.1"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.5.1"

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'
     */
}

