plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.parcelize)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
    alias(libs.plugins.com.google.dagger.hilt.android)
}

android {
    namespace = "com.fantory.knightgame"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fantory.knightgame"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    //Standard Dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.cardview)

    //Kotlin Fragment
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.preference)
    implementation(libs.android.things)
    implementation(libs.androidx.test)

    //Unit test related libraries
    testImplementation(libs.junit)
    testImplementation(libs.androidx.arch.core)
    testImplementation(libs.org.jetbrains.kotlinx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.com.jakewharton.espresso)
    androidTestImplementation(libs.com.jakewharton.espresso)

    //Data binding
    kapt(libs.databinding.compiler)

    //ViewModel
    implementation(libs.androidx.lifecycle)

    //LiveData
    implementation(libs.androidx.livedata)

    // Dagger Hilt
    implementation(libs.com.google.dagger)
    implementation(libs.androidx.hilt)
    kapt(libs.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    //Room Database
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.rxjava2)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
}