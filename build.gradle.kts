import io.ktor.plugin.features.DockerImageRegistry
import io.ktor.plugin.features.DockerPortMapping
import io.ktor.plugin.features.DockerPortMappingProtocol
import io.ktor.plugin.features.JreVersion
import org.jetbrains.kotlin.konan.properties.Properties

val ktorVersion: String = project.properties["ktor.version"] as String
val kotlinVersion: String = project.properties["kotlin.version"] as String
val logbackVersion: String = project.properties["logback.version"] as String
val exposedVersion: String = project.properties["exposed.version"] as String
val h2Version: String = project.properties["h2.version"] as String
val sqliteVersion: String = project.properties["sqlite.version"] as String
val detektVersion: String = project.properties["detekt.version"] as String
val qrCodeKotlinVersion: String = project.properties["qrCodeKotlin.version"] as String
val zxingVersion: String = project.properties["zxing.version"] as String
val postgresqlVersion: String = project.properties["postgresql.version"] as String
val sentryVersion: String = project.properties["sentry.version"] as String

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.2"
}

group = "com.arnyminerz.filamagenta"
version = "0.1.1"

application {
    mainClass.set("com.arnyminerz.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(17)
}

val generatedVersionDir = File(buildDir, "generated-version")

sourceSets {
    main {
        output.dir(generatedVersionDir)
    }
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
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.FilaMagenta:Commons:487bf3c")

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
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.g0dkar:qrcode-kotlin-jvm:$qrCodeKotlinVersion")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    implementation("io.sentry:sentry:$sentryVersion")
    implementation("io.sentry:sentry-jdbc:$sentryVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("com.google.zxing:core:$zxingVersion")
    testImplementation("com.google.zxing:javase:$zxingVersion")
}

tasks.register("generateVersionProperties") {
    doLast {
        val propertiesFile = File(generatedVersionDir, "version.properties")
        generatedVersionDir.mkdirs()
        val properties = Properties()
        properties.setProperty("version", version.toString())
        propertiesFile.writer().use { properties.store(it, null) }
    }
}

tasks.getByName("processResources") {
    dependsOn += "generateVersionProperties"
}
