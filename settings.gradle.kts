plugins {
    id("com.gradleup.nmcp.settings").version("1.6.2")
}

rootProject.name = "stellar-sdk"

nmcpSettings {
    centralPortal {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
        // publish manually from the portal
        publishingType = "USER_MANAGED"
        // or if you want to publish automatically
        // publishingType = "AUTOMATIC"
    }
}
