plugins {
    id("java")
    id("io.freefair.lombok") version "9.7.0"
}

group = "eu.senla.components"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val slf4jVersion = "2.0.16"

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.16")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.16")

    implementation("org.seleniumhq.selenium:selenium-java:4.47.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
    environment(
        "APP_USERNAME",
        System.getenv("APP_USERNAME") ?: project.findProperty("APP_USERNAME") ?: ""
    )
    environment(
        "APP_PASSWORD",
        System.getenv("APP_PASSWORD") ?: project.findProperty("APP_PASSWORD") ?: ""
    )
}