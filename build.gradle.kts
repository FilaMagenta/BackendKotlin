import io.ktor.plugin.features.DockerImageRegistry
import io.ktor.plugin.features.DockerPortMapping
import io.ktor.plugin.features.DockerPortMappingProtocol
import io.ktor.plugin.features.JreVersion

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val h2Version: String by project
val jsonVersion: String by project
val sqliteVersion: String by project
val detektVersion: String by project
val qrCodeKotlinVersion: String by project
val zxingVersion: String by project

plugins {
    kotlin("jvm") version "1.8.22"
    id("io.ktor.plugin") version "2.3.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.1"
}

group = "com.arnyminerz.filamagenta"
version = "0.1.0"

application {
    mainClass.set("com.arnyminerz.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(17)
}

ktor {
    docker {
        jreVersion.set(JreVersion.JRE_17)
        localImageName.set("filamagenta")
        imageTag.set(version.toString())
        portMappings.set(
            listOf(
                DockerPortMapping(80, 8080, DockerPortMappingProtocol.TCP)
            )
        )

        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { "filamagenta" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}

detekt {
    toolVersion = detektVersion
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")
    implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-double-receive-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.json:json:$jsonVersion")
    implementation("io.github.g0dkar:qrcode-kotlin-jvm:$qrCodeKotlinVersion")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("com.google.zxing:core:$zxingVersion")
    testImplementation("com.google.zxing:javase:$zxingVersion")
}
