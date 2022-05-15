import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
}

group = "io.4baba"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    toolVersion = "1.17.1"
    failFast = true
    buildUponDefaultConfig = true
    input = files("$projectDir/src/commonMain")
    config = files("$projectDir/config/detekt.yml")

    reports.html {
        enabled = true
        destination = file("$projectDir/reports/detekt.html")
    }
}
