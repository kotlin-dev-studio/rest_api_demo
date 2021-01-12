import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.21"

    java
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
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
    jcenter()
}

val springBootVersion = "2.4.0"

dependencies {
    // internal modules
    implementation(project(":common"))
    implementation(project(":user"))
    implementation(project(":product"))
    implementation(project(":payment"))

    // external libraries

    // Introspecting the structure of your own program at runtime
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")

    implementation("org.springframework.boot:spring-boot-starter-web")

    // Session
    implementation("org.springframework.data:spring-data-redis:$springBootVersion")
    implementation("redis.clients:jedis")
    implementation("org.springframework.session:spring-session-data-redis:$springBootVersion")

    // Adds support for serialization/deserialization classes and data classes
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring Security OAuth
    implementation("org.springframework.security.oauth:spring-security-oauth2:2.4.0.RELEASE")

    // Additional development-time features
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Database
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
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
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        jcenter()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springframework.boot:spring-boot-starter-web")

        // Test API
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(module = "junit")
            exclude(module = "mockito-core")
        }
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("com.ninja-squad:springmockk:1.1.3")
    }

    // spring boot plugin may only be applied to the root project
    dependencyManagement {
        imports { mavenBom("org.springframework.boot:spring-boot-dependencies:${springBootVersion}") }
    }
}

//plugins {
//    val kotlinVersion = "1.4.21"
//    id("org.springframework.boot") version "2.4.0"
//    id("io.spring.dependency-management") version "1.0.10.RELEASE"
//    kotlin("jvm") version kotlinVersion
//    kotlin("plugin.spring") version kotlinVersion
//    // annotated with the all-open meta-annotation
//    kotlin("plugin.allopen") version kotlinVersion
//}
//
//allOpen {
//    annotation("javax.persistence.Entity")
//    annotation("javax.persistence.Embeddable")
//    annotation("javax.persistence.MappedSuperclass")
//}
//
//group = "com.hlk"
//version = "0.0.1-SNAPSHOT"
//java.sourceCompatibility = JavaVersion.VERSION_1_8
//
//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-web")
////    implementation("org.springframework.data:spring-data-redis:1.8.10.RELEASE")
////    implementation("io.lettuce:lettuce-core:5.2.2.RELEASE")
////    implementation("org.springframework.session:spring-session-data-redis:1.3.1.RELEASE")
////    implementation("org.springframework.session:spring-session:1.3.1.RELEASE")
//
//    // Session
//    implementation("org.springframework.data:spring-data-redis:2.4.0")
//    implementation("redis.clients:jedis")
//    implementation("org.springframework.session:spring-session-data-redis:2.4.0")
////    implementation("org.springframework.session:spring-session")
//    // Adds support for serialization/deserialization classes and data classes
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    // Introspecting the structure of your own program at runtime
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//
//    // Spring Security OAuth
//    implementation("org.springframework.security.oauth:spring-security-oauth2:2.4.0.RELEASE")
//
//    // Additional development-time features
//    developmentOnly("org.springframework.boot:spring-boot-devtools")
//
//    // Database
//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    runtimeOnly("mysql:mysql-connector-java")
//
//    // Logging
//    implementation("io.github.microutils:kotlin-logging:1.7.4")
//
//    // Test API
//    testImplementation("org.springframework.boot:spring-boot-starter-test") {
//        exclude(module = "junit")
//        exclude(module = "mockito-core")
//    }
//    testImplementation("org.junit.jupiter:junit-jupiter-api")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
//    testImplementation("com.ninja-squad:springmockk:1.1.3")
////    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.3.2")
//}

configurations {
    // shared module can't depend on self, but all the other projects need it
    subprojects.find { it.name != ":common" }.apply {
        dependencies {
            implementation(project(":common"))
            testImplementation(project(":common", "testRuntimeOnly"))
        }
    }
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
