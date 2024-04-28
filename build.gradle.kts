import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
	kotlin("kapt") version "1.9.23"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.9")
	implementation("org.mapstruct:mapstruct:1.4.2.Final")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	implementation("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-core:9.16.3")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
	//implementation("org.projectlombok:lombok:1.18.22")
	//kapt("org.projectlombok:lombok:1.18.22")
	kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	//runtimeOnly("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
