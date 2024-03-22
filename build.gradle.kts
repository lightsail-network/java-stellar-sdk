plugins {
    id("java")
    id("java-library")
    id("jacoco")
    id("maven-publish")
    id("project-report")
    id("com.diffplug.spotless") version "6.24.0"
    id("com.github.ben-manes.versions") version "0.50.0"
    id("io.freefair.lombok") version "8.4"
}

group = "stellar"
version = "0.43.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

spotless {
    java {
        importOrder("java", "javax", "org.stellar")
        removeUnusedImports()
        googleJavaFormat()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val okhttpVersion = "4.12.0"

    implementation("com.squareup.okhttp3:okhttp:${okhttpVersion}")
    implementation("com.squareup.okhttp3:okhttp-sse:${okhttpVersion}")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.i2p.crypto:eddsa:0.3.0")
    implementation("commons-codec:commons-codec:1.16.0")

    testImplementation("org.mockito:mockito-core:5.9.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:${okhttpVersion}")
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    test {
        useJUnitPlatform()
    }

    jar {
        manifest {
            attributes["Implementation-Title"] = "stellar-sdk"
            attributes["Implementation-Version"] = version
        }
        archiveFileName = "stellar-sdk.jar"
    }

    val sourcesJar by creating(Jar::class) {
        manifest {
            attributes["Implementation-Title"] = "stellar-sdk"
            attributes["Implementation-Version"] = version
        }
        archiveClassifier = "sources"
        archiveFileName = "stellar-sdk-sources.jar"
        from(sourceSets.main.get().allSource)
    }

    val uberJar by creating(Jar::class) {
        // https://docs.gradle.org/current/userguide/working_with_files.html#sec:creating_uber_jar_example
        manifest {
            attributes["Implementation-Title"] = "stellar-sdk"
            attributes["Implementation-Version"] = version
        }
        archiveClassifier = "uber"
        archiveFileName = "stellar-sdk-uber.jar"
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }

    javadoc {
        destinationDir = file("javadoc")
        isFailOnError = false
        exclude("org/stellar/sdk/xdr/**")
        options {
            // https://docs.gradle.org/current/javadoc/org/gradle/external/javadoc/StandardJavadocDocletOptions.html
            this as StandardJavadocDocletOptions
            isSplitIndex = true
            memberLevel = JavadocMemberLevel.PUBLIC
            encoding = "UTF-8"
        }
    }

    val javadocJar by creating(Jar::class) {
        manifest {
            attributes["Implementation-Title"] = "stellar-sdk"
            attributes["Implementation-Version"] = version
        }
        archiveClassifier = "javadoc"
        archiveFileName = "stellar-sdk-javadoc.jar"
        dependsOn(javadoc)
        from(javadoc.get().destinationDir) // It needs to be placed after the javadoc task, otherwise it cannot read the path we set.
    }

    register<Copy>("updateGitHook") {
        from("scripts/pre-commit.sh") { rename { it.removeSuffix(".sh") } }
        into(".git/hooks")
        doLast {
            file(".git/hooks/pre-commit").setExecutable(true)
        }
    }


    jacocoTestReport {
        reports {
            html.required = true
            xml.required = true
        }
        classDirectories.setFrom(files(classDirectories.files.map { fileTree(it) {
            exclude("org/stellar/sdk/xdr/**")
        } }))
    }

    check {
        dependsOn(jacocoTestReport)
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }
}

artifacts {
    archives(tasks.jar)
    archives(tasks["uberJar"])
    archives(tasks["javadocJar"])
    archives(tasks["sourcesJar"])
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["uberJar"])
            artifact(tasks["javadocJar"])
            artifact(tasks["sourcesJar"])
            pom {
                name.set("java-stellar-sdk")
                description.set("The Java Stellar SDK library provides APIs to build transactions and connect to Horizon and Soroban-RPC server.")
                url.set("https://github.com/stellar/java-stellar-sdk")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://github.com/stellar/java-stellar-sdk/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("stellar")
                        name.set("Stellar Development Foundation")
                    }
                }
            }
        }
    }
}
