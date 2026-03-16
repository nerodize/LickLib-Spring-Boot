plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.11.4"
}

group = "de.seitz"
version = "0.0.1-SNAPSHOT"
description = "project for Spring Backend"

val mockitoAgent = configurations.create("mockitAgent")

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.flywaydb:flyway-core")
	//implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.flywaydb:flyway-database-postgresql")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.mockito:mockito-core:5.23.0") // Deine Version
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	// scheinbar besser testing entkoppelt von postgres => in mem db
	testImplementation("com.h2database:h2")
	mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:junit-jupiter")
}

tasks.withType<Test> {
	useJUnitPlatform()

	jvmArgs("-XX:+EnableDynamicAgentLoading")
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

tasks.withType<JavaCompile>().configureEach {
	// Schaltet die Details für "nicht geprüfte Vorgänge" frei,
	// damit du siehst, WO das Problem im Code liegt.
	options.compilerArgs.add("-Xlint:-unchecked")
	options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()

	// ... deine bisherigen jvmArgs für den Agenten ...

	// LÖST DIE WARNUNG: Deaktiviert das Class Sharing,
	// das mit dem Bootstrap-Classpath kollidiert.
	jvmArgs("-Xshare:off")
}

// Speziell für den AOT Task, der oben in deinem Log auftaucht:
tasks.withType<org.springframework.boot.gradle.tasks.aot.ProcessAot>().configureEach {
	// Verhindert die Sharing-Warnung während der AOT-Verarbeitung
	jvmArgs("-Xshare:off")
}
