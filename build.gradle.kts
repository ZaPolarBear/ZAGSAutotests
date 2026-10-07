plugins {
    id("java")
    id("io.freefair.lombok") version "9.7.0"
    id("io.qameta.allure") version "4.1.0"
}

group = "eu.senla.components"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val slf4jVersion = "2.0.16"
val allureVersion = "2.35.3"
val aspectjVersion = "1.9.25"
val restAssuredVersion = "5.5.0"
val jacksonVersion = "2.18.2"
val fakerVersion = "1.0.2"

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.16")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.16")

    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
    implementation("org.seleniumhq.selenium:selenium-java:4.47.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-jupiter")

    testImplementation("org.aspectj:aspectjweaver:$aspectjVersion")

    testImplementation("com.github.javafaker:javafaker:${fakerVersion}")

    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")
    testImplementation("io.rest-assured:json-schema-validator:${restAssuredVersion}")
}

allure {
    version = "3.9.0"
    report {
        singleFile.set(true)
    }
}

tasks.test {
    useJUnitPlatform()

    environment(
        "APP_USERNAME",
        System.getenv("APP_USERNAME") ?: project.findProperty("APP_USERNAME") ?: ""
    )
    environment(
        "APP_PASSWORD",
        System.getenv("APP_PASSWORD") ?: project.findProperty("APP_PASSWORD") ?: ""
    )
    jvmArgs(
        "-javaagent:${classpath.find { it.name.contains("aspectjweaver") }?.absolutePath}"
    )

    systemProperty("allure.results.directory",
        layout.buildDirectory.dir("allure-results").get().asFile.absolutePath)

    finalizedBy("allureReport")
}