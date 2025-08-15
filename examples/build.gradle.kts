plugins {
    id("java")
    id("com.diffplug.spotless") version "6.24.0"

}

group = "network.lightsail"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

spotless {
    java {
        importOrder("java", "javax", "org.stellar")
        removeUnusedImports()
        googleJavaFormat()
    }
}

dependencies {
    // Use https://central.sonatype.com/artifact/network.lightsail/stellar-sdk in prod.
    implementation("network.lightsail:stellar-sdk:2.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}