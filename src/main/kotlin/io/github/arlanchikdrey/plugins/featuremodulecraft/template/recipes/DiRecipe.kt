package io.github.arlanchikdrey.plugins.featuremodulecraft.template.recipes

import com.android.tools.idea.wizard.template.RecipeExecutor
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model.HiltModuleModel
import io.github.arlanchikdrey.plugins.featuremodulecraft.ext.saveFile

@Deprecated("Wizard will no longer be supported")
fun RecipeExecutor.createDiFiles(
    srcPath: String,
    packageName: String,
    featureName: String
) {
    val diDir = "${srcPath}/di"
    val diPackageName = "${packageName}.di"

    val bindModuleContent = HiltModuleModel.get(
        packageName = diPackageName,
        className = featureName
    )
    saveFile(
        absolutePath = diDir,
        relative = "${featureName}BindModule.kt",
        content = bindModuleContent
    )
}