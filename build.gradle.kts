import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
//    id("com.google.cloud.tools.appengine") version "2.4.2"
//    id("org.jlleitschuh.gradle.ktlint") version "10.1.0" // https://github.com/JLLeitschuh/ktlint-gradle
}

group = "com.muebles.ra"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.muebles.ra.MainKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("javax.inject:javax.inject:1")
    implementation(group = "org.springframework", name = "spring-context", version = "5.3.5")
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.8.8")

    //google dependencies
    implementation("com.google.cloud:google-cloud-storage:2.0.2")
    implementation(platform("com.google.cloud:libraries-bom:21.0.0"))
    implementation("com.google.cloud:google-cloud-secretmanager:1.7.2")
    implementation("com.google.firebase:firebase-messaging:22.0.0")
    implementation("com.google.firebase:firebase-admin:8.0.1")
    implementation("com.google.cloud:google-cloud-pubsub:1.114.4")

    runtimeOnly("com.google.gms:google-services:4.2.0")

    compileOnly("org.projectlombok:lombok:1.18.20")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
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

val shadowJar: com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar by tasks

shadowJar.apply {
    archiveFileName.set("furniture-repository.jar")
    manifest {
        attributes["Main-Class"] = "com.muebles.ra.MainKt"
    }
}

//appengine {
//    stage {
//        setArtifact("build/libs/furniture-repository.jar")
//        setAppEngineDirectory("appengine")
//    }
//    deploy {
//        version = "GCLOUD_CONFIG"
//        projectId = "GCLOUD_CONFIG"
//    }
//}
