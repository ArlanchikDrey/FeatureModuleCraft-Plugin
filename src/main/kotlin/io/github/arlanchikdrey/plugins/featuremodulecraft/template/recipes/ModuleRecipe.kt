package io.github.arlanchikdrey.plugins.featuremodulecraft.template.recipes

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor

@Deprecated("Wizard will no longer be supported")
fun RecipeExecutor.createModuleFiles(
    moduleTempData: ModuleTemplateData,
    featureName: String,
    diFilesEnabled: Boolean
) {
    val moduleName = moduleTempData.name.split(":").last()
    val packageName = moduleTempData.packageName

    val apiRootPath = moduleTempData.rootDir.absolutePath.replaceFirst(moduleName, "$moduleName-api")

    val implRootPath = moduleTempData.rootDir.absolutePath
        .replaceFirst(moduleName, "$moduleName-impl")
    val implSrcPath = moduleTempData.srcDir.absolutePath
        .replace("java", "kotlin")
        .replaceFirst(moduleName, "$moduleName-impl")

    removeFiles(moduleTempData)

    // adding new modules to settings.gradle
    this.addIncludeToSettings("$moduleName-api")
    this.addIncludeToSettings("$moduleName-impl")

    createBuildGradle(
        implRootPath = implRootPath,
        apiRootPath = apiRootPath,
        moduleName = moduleName,
        packageName = packageName,
        diEnabled = diFilesEnabled
    )

    if (diFilesEnabled) {
        createDiFiles(
            srcPath = implSrcPath,
            packageName = moduleTempData.packageName,
            featureName = featureName
        )
    }
}

// Delete automatically generated files
private fun removeFiles(templateData: ModuleTemplateData) {
    with(templateData) {
        resDir.deleteRecursively()
        manifestDir.deleteRecursively()
        rootDir.resolve("build.gradle").delete()
        rootDir.resolve("build.gradle.kts").delete()
        rootDir.resolve("proguard-rules.pro").delete()
        rootDir.resolve("libs").delete()
        rootDir.deleteRecursively()
    }
}