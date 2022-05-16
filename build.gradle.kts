import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion = "2.0.1"
val detektVersion = "1.20.0"
val logbackVersion = "1.2.11"

plugins {
    application
    kotlin("jvm") version "1.6.21"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    kotlin("plugin.serialization") version "1.6.10"
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

group = "io.4baba"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-cbor:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // For development
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    toolVersion = detektVersion
    failFast = true
    buildUponDefaultConfig = true
    input = files("$projectDir/src/commonMain")
    config = files("$projectDir/config/detekt.yml")

    reports.html {
        enabled = true
        destination = file("$projectDir/reports/detekt.html")
    }
}
