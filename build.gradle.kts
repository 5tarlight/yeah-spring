plugins {
    kotlin("jvm") version "2.0.20"
}

group = "io.yeahx4"
version = "alpha-1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.1")
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("ch.qos.logback:logback-classic:1.5.8")
    implementation("ch.qos.logback:logback-core:1.5.8")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
