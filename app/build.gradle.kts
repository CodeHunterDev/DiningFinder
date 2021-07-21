/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.secrets_gradle_plugin") version "0.6.1"
}

android {
    compileSdk = Versions.COMPILE_SDK
    buildToolsVersion = "30.0.3"
    defaultConfig {
        applicationId = "com.elbehiry.diningfinder"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.versionCodeMobile
        versionName = Versions.versionName
        testInstrumentationRunner = "com.elbehiry.diningfinder.CustomTestRunner"

        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["dagger.hilt.disableModulesHaveInstallInCheck"] = "true"
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue(
                "string",
                "google_maps_key",
                "AIzaSyCeWgfOFYvG7PYq4tfKUOFw5LTp4-eCBG8"
            )
        }
        getByName("debug") {
            versionNameSuffix = "-debug"
            resValue(
                "string",
                "google_maps_key",
                "AIzaSyCeWgfOFYvG7PYq4tfKUOFw5LTp4-eCBG8"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    kotlinOptions {
        val options = this
        options.jvmTarget = "1.8"
    }

    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
    }
}

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":shared"))
    testImplementation(project(":test-shared"))
    implementation(project(":model"))

    implementation(Libs.CORE_KTX)
    // UI
    implementation(Libs.ACTIVITY_KTX)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.FRAGMENT_KTX)
    implementation(Libs.CARDVIEW)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.DRAWER_LAYOUT)
    implementation(Libs.MATERIAL)

    // Architecture Components
    testImplementation(Libs.ARCH_TESTING)
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.NAVIGATION_UI_KTX)
    implementation(Libs.LIFECYCLE_RUNTIME_KTX)
    kapt(Libs.LIFECYCLE_COMPILER)

    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)
    androidTestImplementation(Libs.HILT_TESTING)
    kaptAndroidTest(Libs.HILT_COMPILER)

    // Glide
    implementation(Libs.GLIDE)
    kapt(Libs.GLIDE_COMPILER)

    // Kotlin
    implementation(Libs.KOTLIN_STDLIB)

    // Instrumentation tests
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.ESPRESSO_CONTRIB)
    androidTestImplementation(Libs.EXT_JUNIT)
    androidTestImplementation(Libs.RUNNER)
    androidTestImplementation(Libs.RULES)
    androidTestImplementation(Libs.FRAGMENT_TEST)
    androidTestImplementation(Libs.NAVIGATION_TESTING)
    debugImplementation(Libs.FRAGMENT_TEST)

    // Local unit tests
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.HAMCREST)
    testImplementation(Libs.FAKER)
    testImplementation(Libs.TURBINE)
    testImplementation(Libs.EXT_JUNIT)
    testImplementation(Libs.ASSERT_J)
    testImplementation(Libs.MOCKK)

    // play service
    implementation(Libs.COROUTINES_PLAY_SERVICE)
    implementation(Libs.PLAY_SERVICE_LOCATION)

    // Maps
    api(Libs.GOOGLE_MAP_UTILS_KTX) {
        exclude(group = "com.google.android.gms")
    }
    api(Libs.GOOGLE_PLAY_SERVICES_MAPS_KTX)
}
apply(plugin = "com.google.gms.google-services")
