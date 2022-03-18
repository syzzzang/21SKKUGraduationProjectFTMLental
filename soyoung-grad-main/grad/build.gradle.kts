import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
	kotlin("plugin.jpa") version "1.5.21"
	kotlin("kapt") version "1.3.61"
}

group = "com.soyoung"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
val qeurydslVersion = "4.4.0"

repositories {
	mavenCentral()
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
	kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	//swagger
	implementation("io.springfox:springfox-boot-starter:3.0.0")

	//db
	implementation("mysql:mysql-connector-java:8.0.23")

	// querydsl
	implementation("com.querydsl:querydsl-jpa:$qeurydslVersion")
	kapt("com.querydsl:querydsl-apt:$qeurydslVersion:jpa")
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	// redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.data:spring-data-redis")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
