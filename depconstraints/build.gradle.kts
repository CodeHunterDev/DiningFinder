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
    id("java-platform")
    id("maven-publish")
}

val appcompat = "1.1.0"
val activity = "1.2.0-rc01"
val activityCompose = "1.3.0-alpha03"
val appStartup = "1.1.0-beta01"
val cardview = "1.0.0"
val archTesting = "2.0.0"
val arcore = "1.7.0"
val benchmark = "1.0.0"
val benchmarkMacro = "1.1.0-alpha02"
val browser = "1.0.0"
val constraintLayout = "1.1.3"
val core = "1.3.2"
val coroutines = "1.4.2"
val coroutinesTest = "1.3.4"
val crashlytics = "17.2.2"
val dataStore = "1.0.0-beta01"
val drawerLayout = "1.1.0-rc01"
val espresso = "3.1.1"
val fragment = "1.3.0"
val glide = "4.9.0"
val googlePlayServicesMapsKtx = "3.0.0"
val googlePlayServicesVision = "17.0.2"
val gson = "2.8.6"
val hamcrest = "1.3"
val hilt = Versions.HILT_AGP
val junit = "4.13"
val junitExt = "1.1.1"
val lifecycle = "2.4.0-alpha01"
val lottie = "3.0.0"
val material = "1.4.0-beta01"
val mockito = "3.11.2"
val mockitoKotlin = "1.5.0"
val okhttp = "3.10.0"
val okio = "1.14.0"
val pageIndicator = "1.3.0"
val playCore = "1.6.5"
val room = "2.2.5"
val rules = "1.1.1"
val runner = "1.2.0"
val slidingpanelayout = "1.2.0-alpha01"
val threetenabp = "1.0.5"
val timber = "4.7.1"
val viewpager2 = "1.0.0"
val viewModelCompose = "1.0.0-alpha02"
val uiAutomator = "2.2.0"
val retrofit = "2.9.0"
val moshi = "1.11.0"
val kotchi = "2.3.3"
val faker = "1.0.2"
val binder = "1.0.0-alpha01"
val turbine = "0.5.2"
val assertJVersion = "3.19.0"
val playServiceLocation = "18.0.0"
val mockkVersion = "1.10.6"

dependencies {
    constraints {
        api("${Libs.ACTIVITY_COMPOSE}:$activityCompose")
        api("${Libs.ACTIVITY_KTX}:$activity")
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.CARDVIEW}:$cardview")
        api("${Libs.ARCH_TESTING}:$archTesting")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraintLayout")
        api("${Libs.CORE_KTX}:$core")
        api("${Libs.COROUTINES}:$coroutines")
        api("${Libs.COROUTINES_TEST}:$coroutines")
        api("${Libs.COROUTINES_PLAY_SERVICE}:$coroutines")
        api("${Libs.DRAWER_LAYOUT}:$drawerLayout")
        api("${Libs.ESPRESSO_CORE}:$espresso")
        api("${Libs.ESPRESSO_CONTRIB}:$espresso")
        api("${Libs.FRAGMENT_KTX}:$fragment")
        api("${Libs.FRAGMENT_TEST}:$fragment")
        api("${Libs.GLIDE}:$glide")
        api("${Libs.GLIDE_COMPILER}:$glide")
        api("${Libs.GOOGLE_MAP_UTILS_KTX}:$googlePlayServicesMapsKtx")
        api("${Libs.GOOGLE_PLAY_SERVICES_MAPS_KTX}:$googlePlayServicesMapsKtx")
        api("${Libs.GOOGLE_PLAY_SERVICES_VISION}:$googlePlayServicesVision")
        api("${Libs.GSON}:$gson")
        api("${Libs.HAMCREST}:$hamcrest")
        api("${Libs.HILT_ANDROID}:$hilt")
        api("${Libs.HILT_COMPILER}:$hilt")
        api("${Libs.HILT_TESTING}:$hilt")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.EXT_JUNIT}:$junitExt")
        api("${Libs.KOTLIN_STDLIB}:${Versions.KOTLIN}")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.MOCKITO_CORE}:$mockito")
        api("${Libs.MOCKITO_KOTLIN}:$mockitoKotlin")
        api("${Libs.NAVIGATION_FRAGMENT_KTX}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_UI_KTX}:${Versions.NAVIGATION}")
        api("${Libs.OKHTTP}:$okhttp")
        api("${Libs.OKHTTP_LOGGING_INTERCEPTOR}:$okhttp")
        api("${Libs.RULES}:$rules")
        api("${Libs.RUNNER}:$runner")
        api("${Libs.TIMBER}:$timber")
        api("${Libs.OKHTTP_MOCK_SERVER}:$okhttp")
        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.MOSHI}:$moshi")
        api("${Libs.MOSHI_RETROFIT}:$retrofit")
        api("${Libs.MOSHI_KOTLIN}:$moshi")
        api("${Libs.KOTCHI}:$kotchi")
        api("${Libs.KOTCHI_COMPILER}:$kotchi")
        api("${Libs.FAKER}:$faker")
        api("${Libs.HILT_BINDER}:$binder")
        api("${Libs.HILT_BINDER_COMPILER}:$binder")
        api("${Libs.TURBINE}:$turbine")
        api("${Libs.ASSERT_J}:$assertJVersion")
        api("${Libs.LIFECYCLE_COMPILER}:$lifecycle")
        api("${Libs.LIFECYCLE_RUNTIME_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.PLAY_SERVICE_LOCATION}:$playServiceLocation")
        api("${Libs.MOCKK}:$mockkVersion")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
