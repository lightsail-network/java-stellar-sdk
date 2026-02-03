plugins {
    id("java")
    id("jacoco")
    id("signing")
    id("maven-publish")
    id("project-report")
    id("com.diffplug.spotless") version "7.2.1"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("io.freefair.lombok") version "8.14"
    id("com.gradleup.nmcp.aggregation").version("1.0.2")
    kotlin("jvm") version "2.2.0"
}

group = "network.lightsail"
version = "2.2.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

kotlin {
    jvmToolchain(8)
}

spotless {
    java {
        importOrder("java", "javax", "org.stellar")
        removeUnusedImports()
        googleJavaFormat()
    }
    kotlin {
        target("src/test/kotlin/**/*.kt")
        ktfmt("0.56").googleStyle()
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
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("org.bouncycastle:bcprov-jdk18on:1.81")
    implementation("commons-codec:commons-codec:1.19.0")

    testImplementation(kotlin("stdlib"))
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:${okhttpVersion}")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.bouncycastle:bcpkix-jdk18on:1.79")  // mock https
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-framework-datatest:5.9.1")
}

tasks {
    test {
        useJUnitPlatform()
    }

    val sourcesJar by creating(Jar::class) {
        archiveClassifier = "sources"
        from(sourceSets.main.get().allSource)
    }

    val uberJar by creating(Jar::class) {
        // https://docs.gradle.org/current/userguide/working_with_files.html#sec:creating_uber_jar_exampl
        archiveClassifier = "uber"
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA") // exclude signature files in org.bouncycastle:bcprov-jdk18on
    }

    javadoc {
        setDestinationDir(file("javadoc"))
        isFailOnError = false
        options {
            // https://docs.gradle.org/current/javadoc/org/gradle/external/javadoc/StandardJavadocDocletOptions.html
            this as StandardJavadocDocletOptions
            isSplitIndex = true
            memberLevel = JavadocMemberLevel.PUBLIC
            encoding = "UTF-8"
        }
    }

    val javadocJar by creating(Jar::class) {
        archiveClassifier = "javadoc"
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
        dependsOn(compileJava, compileKotlin, processResources)
        reports {
            html.required = true
            xml.required = true
        }
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                exclude("org/stellar/sdk/xdr/**")
            }
        }))
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
            artifactId = "stellar-sdk"
            from(components["java"])
            artifact(tasks["uberJar"])
            artifact(tasks["javadocJar"])
            artifact(tasks["sourcesJar"])
            pom {
                name.set("stellar-sdk")
                description.set("The Java Stellar SDK library provides APIs to build transactions and connect to Horizon and Soroban-RPC server.")
                url.set("https://github.com/lightsail-network/java-stellar-sdk")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://github.com/lightsail-network/java-stellar-sdk/blob/master/LICENSE")
                        distribution.set("https://github.com/lightsail-network/java-stellar-sdk/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("overcat")
                        name.set("Jun Luo")
                        url.set("https://github.com/overcat")
                    }
                    organization {
                        name.set("Lightsail Network")
                        url.set("https://github.com/lightsail-network")
                    }
                }
                scm {
                    url.set("https://github.com/lightsail-network/java-stellar-sdk")
                    connection.set("scm:git:https://github.com/lightsail-network/java-stellar-sdk.git")
                    developerConnection.set("scm:git:ssh://git@github.com/lightsail-network/java-stellar-sdk.git")
                }
            }
        }
    }
}


signing {
    val publishCommand = "publishAllPublicationsToCentralPortal"
    isRequired = gradle.startParameter.taskNames.contains(publishCommand)
    println("Need to sign? $isRequired")
    // https://docs.gradle.org/current/userguide/signing_plugin.html#using_in_memory_ascii_armored_openpgp_subkeys
    // export SIGNING_KEY=$(gpg2 --export-secret-keys --armor {SIGNING_KEY_ID} | grep -v '\-\-' | grep -v '^=.' | tr -d '\n')
    val signingKey = System.getenv("SIGNING_KEY")
    val signingKeyId = System.getenv("SIGNING_KEY_ID")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    if (isRequired && (signingKey == null || signingKeyId == null || signingPassword == null)) {
        throw IllegalStateException("Please set the SIGNING_KEY, SIGNING_KEY_ID, and SIGNING_PASSWORD environment variables.")
    }
    println("Signing Key ID: $signingKeyId")
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}

nmcpAggregation {
    centralPortal {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
        // publish manually from the portal
        publishingType = "USER_MANAGED"
        // or if you want to publish automatically
        // publishingType = "AUTOMATIC"
    }

    // Publish all projects that apply the 'maven-publish' plugin
    publishAllProjectsProbablyBreakingProjectIsolation()
}
