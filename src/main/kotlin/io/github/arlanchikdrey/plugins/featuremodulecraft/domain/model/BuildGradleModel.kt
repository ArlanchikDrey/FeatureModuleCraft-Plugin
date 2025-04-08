package io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model

import io.github.arlanchikdrey.plugins.featuremodulecraft.ext.appendIf

typealias ApiBuildGradleModel = String
typealias ImplBuildGradleModel = String

object BuildGradleModel {
    fun api(packageName: String): ApiBuildGradleModel {
        return """
        plugins {
            alias(libs.plugins.android.library)
            alias(libs.plugins.kotlin.android)
        }
        
        android {
            namespace = "$packageName"
            compileSdk = 35
            
            defaultConfig {
                minSdk = 24
                consumerProguardFiles("consumer-rules.pro")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        
        dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
        }
    """.trimIndent()
    }

    fun impl(
        packageName: String,
        apiProjectPath: String,
        diEnabled: Boolean
    ): ImplBuildGradleModel {
        return """
        plugins {
            alias(libs.plugins.android.library)
            alias(libs.plugins.kotlin.android)
            ${"alias(libs.plugins.ksp)".appendIf(diEnabled)}
        }
        
        android {
            namespace = "$packageName"
            compileSdk = 35
            
            defaultConfig {
                minSdk = 24
                consumerProguardFiles("consumer-rules.pro")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        
        dependencies {
            implementation(project("$apiProjectPath"))
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            
            ${"implementation(libs.dagger.hilt)".appendIf(diEnabled)}
            ${"ksp(libs.dagger.hilt.compiler)".appendIf(diEnabled)}
            
            testImplementation(libs.junit)
        }
    """.trimIndent()
    }
}