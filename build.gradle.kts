import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.21"
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    // annotated with the all-open meta-annotation
    kotlin("plugin.allopen") version kotlinVersion
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

group = "com.hlk"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.data:spring-data-redis:1.8.10.RELEASE")
//    implementation("io.lettuce:lettuce-core:5.2.2.RELEASE")
//    implementation("org.springframework.session:spring-session-data-redis:1.3.1.RELEASE")
//    implementation("org.springframework.session:spring-session:1.3.1.RELEASE")

    // Session
    implementation("org.springframework.data:spring-data-redis:2.4.0")
    implementation("redis.clients:jedis")
    implementation("org.springframework.session:spring-session-data-redis:2.4.0")
//    implementation("org.springframework.session:spring-session")
    // Adds support for serialization/deserialization classes and data classes
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // Introspecting the structure of your own program at runtime
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring Security JWT
//    implementation("com.auth0:java-jwt:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-security:2.4.0")
    implementation("org.springframework.security:spring-security-jwt:1.0.3.RELEASE")
    implementation("io.jsonwebtoken:jjwt:0.9.1")


    // Additional development-time features
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Database
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("mysql:mysql-connector-java")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.7.4")

    // Test API
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.ninja-squad:springmockk:1.1.3")
//    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.3.2")

    // Add Swagger
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    doFirst {
        systemProperty("spring.profiles.active", "test")
    }
    useJUnitPlatform()
}
