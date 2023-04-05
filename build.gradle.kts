plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.mw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(8)
    sourceSets {
        val main by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
            }
        }
    }
}

application {
    mainClass.set("org.mw.WordsGenerator")
}