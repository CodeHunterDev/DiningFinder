import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT_AGP}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
        classpath("com.google.gms:google-services:${Versions.GOOGLE_SERVICES}")
    }
}

plugins {
    id("com.diffplug.spotless") version "5.10.0"
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    val ktlintVer = "0.40.0"
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(ktlintVer).userData(
                mapOf("max_line_length" to "100", "disabled_rules" to "import-ordering")
            )
            licenseHeaderFile(project.rootProject.file("spotless/copyright.kt"))
        }
        kotlinGradle {
            // same as kotlin, but for .gradle.kts files (defaults to '*.gradle.kts')
            target("**/*.gradle.kts")
            ktlint(ktlintVer)
            licenseHeaderFile(project.rootProject.file("spotless/copyright.kt"), "(plugins |import |include)")
        }
    }

    tasks.whenTaskAdded {
        if (name == "preBuild") {
            mustRunAfter("spotlessCheck")
        }
    }

    // TODO: Remove when the Coroutine and Flow APIs leave experimental/internal/preview.
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}