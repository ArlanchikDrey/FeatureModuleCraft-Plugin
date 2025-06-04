import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm)
    id("org.jetbrains.intellij.platform") version "2.4.0"
    alias(libs.plugins.detekt)
}

group = "io.github.arlanchikdrey.plugins"
version = "0.0.10"

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
    detektPlugins(libs.detekt.formattig)
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellijPlatform {
    projectName = project.name

    pluginConfiguration {
        id = "feature-module-craft-id"
        name = "FeatureModuleCraft"
        version = "0.0.10"
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

configureDetekt()

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

fun Project.configureDetekt() {
    val configPath = "${project.rootDir}/config/detekt/detekt.yml"
    val baselinePath = "${project.rootDir}/config/detekt/baseline.xml"
    val kotlinFiles = "**/*.kt"
    val resourceFiles = "**/resources/**"
    val buildFiles = "**/build/**"

    detekt {
        config.setFrom(configPath)
        baseline = file(baselinePath)
        autoCorrect = false
    }

    tasks.withType<Detekt>().configureEach {
        include(kotlinFiles)
        exclude(resourceFiles, buildFiles)
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }

    val projectSource = file(projectDir)
    val configFile = files(configPath)

    tasks.register("detektFix", Detekt::class) {
        ignoreFailures = false
        autoCorrect = true
        buildUponDefaultConfig = true
        setSource(projectSource)
        config.setFrom(configFile)
        include(kotlinFiles)
        exclude(resourceFiles, buildFiles)
        reports {
            html.required.set(true)
            xml.required.set(false)
            txt.required.set(false)
        }
    }

    tasks.register("detektCreateBaseline", DetektCreateBaselineTask::class) {
        buildUponDefaultConfig = true
        ignoreFailures = true
        setSource(files(rootDir))
        config.setFrom(configFile)
        baseline.set(file(baselinePath))
        include(kotlinFiles)
        exclude(resourceFiles, buildFiles)
    }
}