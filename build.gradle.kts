plugins {
  java
  id("io.quarkus")
  id("io.swagger.core.v3.swagger-gradle-plugin") version "2.2.22"
}

repositories {
  mavenCentral()
  mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
  implementation("io.quarkus:quarkus-security-jpa")
  implementation("io.quarkus:quarkus-smallrye-jwt")
  implementation("io.quarkus:quarkus-smallrye-jwt-build")
  implementation("io.quarkus:quarkus-container-image-docker")
  implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
  implementation("io.quarkus:quarkus-rest")
  implementation("io.quarkus:quarkus-rest-jackson")
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-jdbc-postgresql")
  implementation("io.quarkus:quarkus-smallrye-openapi")
  implementation("io.quarkus:quarkus-hibernate-orm")
  implementation("io.quarkus:quarkus-hibernate-validator")
  implementation("org.mapstruct:mapstruct:1.6.0")

  annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0")
  compileOnly("org.projectlombok:lombok:1.18.34")
  annotationProcessor("org.projectlombok:lombok:1.18.34")

  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.rest-assured:rest-assured")
  testImplementation("org.projectlombok:lombok:1.18.34")
  testImplementation("io.quarkus:quarkus-junit5-mockito")
}

group = "org.hr"
version = "1.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-parameters")
}
