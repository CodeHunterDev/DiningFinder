plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    api(platform(project(":depconstraints")))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // ThreeTenBP for the shared module only. Date and time API for Java.
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.MOSHI)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
