buildscript {
    repositories{
        gradlePluginPortal()
        mavenCentral()
    }
}
allprojects {
    group = "io.entframework.mybatis"
    version = "1.1111-SNAPSHOT"
}
subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

