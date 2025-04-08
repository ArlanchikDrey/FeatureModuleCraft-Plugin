import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij.platform") version "2.4.0"
}

group = "io.github.arlanchikdrey.plugins"
version = "0.0.9"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        androidStudio("2024.2.2.13")
        bundledPlugin("org.jetbrains.android")
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
    projectName = project.name

    pluginConfiguration {
        id = "feature-module-craft-id"
        name = "FeatureModuleCraft"
        version = "0.0.9"
        description = "Creating feature modules api and impl"
        ideaVersion {
            sinceBuild = "242"
            untilBuild = "243.*"
        }
        vendor {
            email = "arlanchikdrey21rus@gmail.com"
            url = "https://github.com/ArlanchikDrey"
            name = "ArlanTech"
        }
        changeNotes = "Minor refactor"
    }

    pluginVerification {
        ides {
            ide(IntelliJPlatformType.AndroidStudio, "2024.2.2.13")
            recommended()
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}
