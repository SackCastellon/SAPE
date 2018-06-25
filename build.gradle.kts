import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.2.50"
    id("org.springframework.boot") version "2.0.3.RELEASE"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.5.RELEASE"
    id("com.github.ben-manes.versions") version "0.20.0"
}

group = "es.uji"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/exposed")
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // Spring
    implementation(spring("starter-web"))
    implementation(spring("starter-jdbc"))
    implementation(spring("starter-actuator"))
    implementation(spring("starter-thymeleaf"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Database
    runtime("org.postgresql:postgresql")
    implementation("org.jetbrains.exposed:exposed:0.10.3")
    implementation("org.jetbrains.exposed:spring-transaction:0.10.3")

    // Webjars
    implementation("org.webjars:jquery:3.3.1-1")
    implementation("org.webjars:popper.js:1.14.1")
    implementation("org.webjars:bootstrap:4.1.1")
    implementation("org.webjars:font-awesome:5.1.0")

    // Testing
    runtime(spring("devtools"))
    testImplementation(spring("starter-test"))
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
    }
}

kotlin.experimental.coroutines = Coroutines.ENABLE

fun DependencyHandler.spring(module: String, version: String? = null): Any =
    "org.springframework.boot:spring-boot-$module${version?.let { ":$version" } ?: ""}"
