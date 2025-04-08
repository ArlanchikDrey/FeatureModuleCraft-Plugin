package io.github.arlanchikdrey.plugins.featuremodulecraft.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import io.github.arlanchikdrey.plugins.featuremodulecraft.bundle.StringsBundle
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.usecase.CreateFeatureModulesUseCase
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.usecase.FeatureNameValidatorUseCase
import io.github.arlanchikdrey.plugins.featuremodulecraft.presentation.CreateFeaturesModuleDialog

class CreateFeaturesModuleAction : AnAction(StringsBundle.get(StringsBundle.KEY_NAME)) {

    private val createFeatureModulesUseCase by lazy(LazyThreadSafetyMode.NONE) { CreateFeatureModulesUseCase() }

    override fun actionPerformed(event: AnActionEvent) {
        val selectedFolder = requireNotNull(event.getData(CommonDataKeys.VIRTUAL_FILE))
        val project = requireNotNull(event.project)
        val projectPath = requireNotNull(project.basePath)

        CreateFeaturesModuleDialog(
            project = project,
            selectedFolder = selectedFolder,
            validatorUseCase = FeatureNameValidatorUseCase()
        ).run {
            if (showAndGet()) {
                val featureName = featureNameTextField?.component?.text.orEmpty()
                val packageName = packageNameTextField?.component?.text.orEmpty()
                val diFilesEnabled = diFilesEnabledCheckBox?.component?.isSelected ?: false
                val domainLayerEnabled = domainLayerEnabledCheckBox?.component?.isSelected ?: false
                val dataLayerEnabled = dataLayerEnabledCheckBox?.component?.isSelected ?: false
                val presentationLayerEnabled = presentationLayerEnabledCheckBox?.component?.isSelected ?: false

                createFeatureModulesUseCase(
                    projectPath = projectPath,
                    parentFolder = selectedFolder,
                    packageName = packageName,
                    featureName = featureName,
                    additionalParameters = CreateFeatureModulesUseCase.AdditionalParameters(
                        diFilesEnabled = diFilesEnabled,
                        domainLayerEnabled = domainLayerEnabled,
                        dataLayerEnabled = dataLayerEnabled,
                        presentationLayerEnabled = presentationLayerEnabled
                    )
                )
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val selectedFiles: Array<VirtualFile>? = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        event.presentation.isEnabled = !selectedFiles.isNullOrEmpty()
    }
}