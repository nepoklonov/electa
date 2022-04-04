plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")

}
val kotlinxSerializationVersion = "1.3.2"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
            }
        }
    }
}

kotlin {
    jvm()
    js {
        browser()
    }
}