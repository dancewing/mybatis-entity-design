plugins {
    id("java")
    `maven-publish`
}


dependencies {
    implementation("com.github.javaparser:javaparser-core:3.25.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
        create<MavenPublication>("mavenJava") {
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}