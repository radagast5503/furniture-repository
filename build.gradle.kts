import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.21"
}

group = "com.muebles.ra"
version = "1.0-SNAPSHOT"
application {
    mainClass.set("com.muebles.ra.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    implementation(kotlin("stdlib"))

    compileOnly("org.projectlombok:lombok:1.18.20")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation(kotlin("test"))

}

tasks.test {
    useJUnit()
}



tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}


val versionDir = "${buildDir}/generated-version"

sourceSets {
    main {
        output.dir(mapOf("builtBy" to "generateVersionProperties"), versionDir)
    }
}

tasks.create("generateVersionProperties") {
    doLast {
        val propertiesFile = file("${versionDir}/version.properties")
        propertiesFile.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", rootProject.version.toString())
        properties.store(propertiesFile.writer(), null)
    }
}

tasks.getting(ProcessResources::class) {
    setDependsOn(setOf("generateVersionProperties"))
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.muebles.ra.MainKt"
    }
}