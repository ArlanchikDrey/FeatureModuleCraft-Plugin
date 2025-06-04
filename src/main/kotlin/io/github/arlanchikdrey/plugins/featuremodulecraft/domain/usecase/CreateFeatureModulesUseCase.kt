package io.github.arlanchikdrey.plugins.featuremodulecraft.domain.usecase

import com.intellij.openapi.vfs.VirtualFile
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model.BuildGradleModel
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model.GitIgnoreModel
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model.HiltModuleModel
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

private const val DATA_FOLDER = "data"
private const val DOMAIN_FOLDER = "domain"
private const val PRESENTATION_FOLDER = "presentation"
private const val DI_FOLDER = "di"

private const val API_SUFFIX = "-api"
private const val IMPL_SUFFIX = "-impl"
private const val KT_SUFFIX = ".kt"

private const val SRC_MAIN_PATH = "src/main/java"
private const val SRC_TEST_PATH = "src/test/java"

private const val BUILD_GRADLE_DSL = "build.gradle.kts"
private const val SETTINGS_GRADLE_DSL = "settings.gradle.kts"
private const val GIT_IGNORE = ".gitignore"

class CreateFeatureModulesUseCase {

    data class AdditionalParameters(
        val diFilesEnabled: Boolean,
        val domainLayerEnabled: Boolean,
        val dataLayerEnabled: Boolean,
        val presentationLayerEnabled: Boolean,
        val composeEnabled: Boolean
    )

    operator fun invoke(
        projectPath: String,
        parentFolder: VirtualFile,
        packageName: String,
        featureName: String,
        additionalParameters: AdditionalParameters
    ) {
        val featureModuleName = featureName.toLowerCase()
        val featureDir = File(parentFolder.path, featureModuleName)
        if (!featureDir.mkdir()) return

        val featureApiPath = createFeatureDir(
            path = featureDir.absolutePath,
            relative = "$featureModuleName$API_SUFFIX",
            buildGradleModel = BuildGradleModel.api(
                packageName = "$packageName.api"
            )
        )
        if (featureApiPath.isNullOrBlank()) return

        createPackageDir(
            parentPath = featureApiPath,
            packageName = "$packageName.api"
        )

        val featureImplPath = createFeatureDir(
            path = featureDir.absolutePath,
            relative = "$featureModuleName$IMPL_SUFFIX",
            buildGradleModel = BuildGradleModel.impl(
                packageName = "$packageName.impl",
                apiProjectPath = ":${parentFolder.name}:$featureModuleName:$featureModuleName$API_SUFFIX",
                diEnabled = additionalParameters.diFilesEnabled,
                composeEnabled = additionalParameters.composeEnabled
            )
        )
        if (featureImplPath.isNullOrBlank()) return

        createPackageImplDirs(
            rootPath = featureImplPath,
            packageName = "$packageName.impl",
            featureName = featureName,
            additionalParameters = additionalParameters
        )

        // adding new modules to settings.gradle.kts
        addIncludeToSettings(
            projectPath = projectPath,
            value = ":${parentFolder.name}:$featureModuleName:$featureModuleName$API_SUFFIX"
        )
        addIncludeToSettings(
            projectPath = projectPath,
            value = ":${parentFolder.name}:$featureModuleName:$featureModuleName$IMPL_SUFFIX"
        )
    }

    private fun createFeatureDir(
        path: String,
        relative: String,
        buildGradleModel: String
    ): String? {
        val dir = File(path).resolve(relative)
        if (!dir.mkdir()) return null

        val absolutePath = dir.absolutePath
        // save build gradle kts
        saveFile(
            path = absolutePath,
            filename = BUILD_GRADLE_DSL,
            content = buildGradleModel
        )
        // save .gitignore
        saveFile(
            path = absolutePath,
            filename = GIT_IGNORE,
            content = GitIgnoreModel.get()
        )

        return absolutePath
    }

    private fun createPackageDir(
        parentPath: String,
        packageName: String,
        includeTestPath: Boolean = false
    ): String? {
        val packagePath = packageName.replace('.', '/')

        val mainFile = File(parentPath).resolve(SRC_MAIN_PATH).resolve(packagePath)
        if (!mainFile.mkdirs()) return null

        if (includeTestPath) {
            File(parentPath).resolve(SRC_TEST_PATH).resolve(packagePath).mkdirs()
        }

        return mainFile.absolutePath
    }

    private fun createPackageImplDirs(
        rootPath: String,
        packageName: String,
        featureName: String,
        additionalParameters: AdditionalParameters
    ) {
        with(additionalParameters) {
            createPackageDir(
                parentPath = rootPath,
                packageName = packageName,
                includeTestPath = true
            )?.let { path ->
                // create layers
                if (domainLayerEnabled) File(path, DOMAIN_FOLDER).mkdir()
                if (dataLayerEnabled) File(path, DATA_FOLDER).mkdir()
                if (presentationLayerEnabled) File(path, PRESENTATION_FOLDER).mkdir()
                // create di files
                if (diFilesEnabled) {
                    val diFile = File(path, DI_FOLDER)
                    if (!diFile.mkdir()) return@let

                    val bindModuleClassName = "${featureName}Module"
                    saveFile(
                        path = diFile.absolutePath,
                        filename = "$bindModuleClassName$KT_SUFFIX",
                        content = HiltModuleModel.get(
                            packageName = "$packageName.$DI_FOLDER",
                            className = bindModuleClassName
                        )
                    )
                }
            }
        }
    }

    private fun saveFile(
        path: String,
        filename: String,
        content: String
    ) {
        val file = File(path).resolve(filename)

        BufferedOutputStream(FileOutputStream(file)).use { stream ->
            stream.write(content.toByteArray())
        }
    }

    private fun addIncludeToSettings(
        projectPath: String,
        value: String
    ) {
        val file = File(projectPath).resolve(SETTINGS_GRADLE_DSL)
        if (!file.isFile) return

        BufferedWriter(FileWriter(file, true)).use { writer ->
            writer.newLine()
            writer.append("include(\"$value\")")
        }
    }
}