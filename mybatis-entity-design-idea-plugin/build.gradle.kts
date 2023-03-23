buildscript {
    repositories{
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.1"
}

val xjcTargetDir by extra(file( "${buildDir}/generated/sources/xjc"))
var xsdDir by extra(file( "${projectDir}/src/main/xsd" ))


repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.github.javaparser:javaparser-core:3.25.1")
    implementation("com.github.jsqlparser:jsqlparser:4.6")
    implementation("org.freemarker:freemarker:2.3.32")
    implementation(project(":mybatis-entity-design-api"))
    testImplementation("com.h2database:h2:1.4.200")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.3.3")
    type.set("IU") // Target IDE Platform
    plugins.set(listOf("java", "uml", "DatabaseTools", "hibernate"))
    //sandboxDir.set("${project.rootDir}/idea-sandbox")
}

configurations.all{
    resolutionStrategy.cacheChangingModulesFor(0, "MINUTES")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    initializeIntelliJPlugin {
        offline.set(true)
    }

    patchPluginXml {
        sinceBuild.set("223")
        untilBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}