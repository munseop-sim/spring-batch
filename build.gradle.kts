import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


object Version{
	const val openFeign = "4.1.0"
	const val queryDsl = "5.1.0"
	const val jacksonModuleKotlin="2.16.1"
	const val feignJackson = "13.2.1"
	const val redisson = "3.27.0"
}

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
	kotlin("kapt") version "1.9.22"
	kotlin("plugin.noarg") version "1.9.22" // noarg 추가를 위함 by reflection
	kotlin("plugin.allopen") version "1.9.22"
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

noArg {
	annotation("jakarta.persistence.Entity")
	annotation("com.fasterxml.jackson.annotation.JsonCreator")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

extra["snippetsDir"] = file("build/generated-snippets")

group = "spring.ms2709"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenLocal()
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-batch")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Version.jacksonModuleKotlin}")
	// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-openfeign
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:${Version.openFeign}")
	implementation("io.github.openfeign:feign-jackson:${Version.feignJackson}")

	implementation("com.querydsl:querydsl-jpa:${Version.queryDsl}:jakarta")
//	implementation("com.querydsl:querydsl-sql:${Version.queryDsl}")
	kapt ("com.querydsl:querydsl-apt:${Version.queryDsl}:jakarta")
	kapt ("jakarta.annotation:jakarta.annotation-api")
	kapt ("jakarta.persistence:jakarta.persistence-api")

	// https://mvnrepository.com/artifact/org.redisson/redisson
	implementation("org.redisson:redisson:${Version.redisson}")


//	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.batch:spring-batch-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}
tasks.withType<JavaCompile>() {
	options.encoding = "UTF-8"
	options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
	useJUnitPlatform()
}


buildscript {
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-noarg")
	}
}
