plugins {
    id("java")
    id("com.diffplug.spotless") version "8.10.4"

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
    implementation("network.lightsail:stellar-sdk:5.0.0")
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}