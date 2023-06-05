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

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.3.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
}

group = "com.arnyminerz.filamagenta"
version = "0.0.1"

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
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.json:json:$jsonVersion")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.1")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.1")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("com.h2database:h2:$h2Version")
}