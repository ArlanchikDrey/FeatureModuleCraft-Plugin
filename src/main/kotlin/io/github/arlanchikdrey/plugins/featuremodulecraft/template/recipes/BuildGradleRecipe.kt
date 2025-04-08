package io.github.arlanchikdrey.plugins.featuremodulecraft.template.recipes

import com.android.tools.idea.wizard.template.RecipeExecutor
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model.BuildGradleModel
import io.github.arlanchikdrey.plugins.featuremodulecraft.ext.saveFile

private const val BUILD_GRADLE_DSL = "build.gradle.kts"

@Deprecated("Wizard will no longer be supported")
fun RecipeExecutor.createBuildGradle(
    implRootPath: String,
    apiRootPath: String,
    moduleName: String,
    packageName: String,
    diEnabled: Boolean
) {
    saveFile(
        absolutePath = implRootPath,
        relative = BUILD_GRADLE_DSL,
        content = BuildGradleModel.impl(
            diEnabled = diEnabled,
            apiProjectPath = moduleName,
            packageName = packageName
        )
    )

    saveFile(
        absolutePath = apiRootPath,
        relative = BUILD_GRADLE_DSL,
        content = BuildGradleModel.api(
            packageName = packageName
        )
    )
}