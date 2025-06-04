package io.github.arlanchikdrey.plugins.featuremodulecraft.presentation

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.BottomGap
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.text
import com.intellij.ui.layout.ValidationInfoBuilder
import io.github.arlanchikdrey.plugins.featuremodulecraft.bundle.StringsBundle
import io.github.arlanchikdrey.plugins.featuremodulecraft.domain.usecase.FeatureNameValidatorUseCase
import javax.swing.JComponent

private const val DEFAULT_PACKAGE_NAME = "com.example.{featureName}"

class CreateFeaturesModuleDialog(
    project: Project,
    private val selectedFolder: VirtualFile?,
    private val validatorUseCase: FeatureNameValidatorUseCase
) : DialogWrapper(project) {

    init {
        title = StringsBundle.get(StringsBundle.KEY_DESCRIPTION)
        super.init()
    }

    var featureNameTextField: Cell<JBTextField>? = null
        private set
    var packageNameTextField: Cell<JBTextField>? = null
        private set
    var diFilesEnabledCheckBox: Cell<JBCheckBox>? = null
        private set
    var domainLayerEnabledCheckBox: Cell<JBCheckBox>? = null
        private set
    var dataLayerEnabledCheckBox: Cell<JBCheckBox>? = null
        private set
    var presentationLayerEnabledCheckBox: Cell<JBCheckBox>? = null
        private set
    var composeEnabledCheckBox: Cell<JBCheckBox>? = null
        private set

    override fun createCenterPanel(): JComponent {
        val featureNameValidator: ValidationInfoBuilder.(JBTextField) -> ValidationInfo? = { textField ->
            val parentFolder = requireNotNull(selectedFolder)

            val result = validatorUseCase(parentFolder, textField.text)

            when(result) {
                FeatureNameValidatorUseCase.ValidatorResult.Success -> null
                FeatureNameValidatorUseCase.ValidatorResult.Empty ->
                    ValidationInfo(StringsBundle.get(StringsBundle.KEY_EMPTY_FIELD))
                FeatureNameValidatorUseCase.ValidatorResult.FeatureAlreadyExists ->
                    ValidationInfo(StringsBundle.get(StringsBundle.KEY_FOLDER_ALREADY_EXISTS))
            }
        }

        return panel {
            row {
                label(StringsBundle.get(StringsBundle.KEY_FEATURE_NAME))
            }
            row {
                featureNameTextField = textField()
                    .focused() //autofocus
                    .validationOnApply(featureNameValidator)
                bottomGap(BottomGap.SMALL)
            }
            row {
                label(StringsBundle.get(StringsBundle.KEY_PACKAGE_NAME))
            }
            row {
                packageNameTextField = textField().text(DEFAULT_PACKAGE_NAME)
                bottomGap(BottomGap.SMALL)
            }
            row {
                diFilesEnabledCheckBox = checkBox(StringsBundle.get(StringsBundle.KEY_DI_FILES_ENABLED))
            }
            row {
                domainLayerEnabledCheckBox = checkBox(StringsBundle.get(StringsBundle.KEY_DOMAIN_LAYER_ENABLED))
            }
            row {
                dataLayerEnabledCheckBox = checkBox(StringsBundle.get(StringsBundle.KEY_DATA_LAYER_ENABLED))
            }
            row {
                presentationLayerEnabledCheckBox =
                    checkBox(StringsBundle.get(StringsBundle.KEY_PRESENTATION_LAYER_ENABLED))
            }
            row {
                composeEnabledCheckBox = checkBox(StringsBundle.get(StringsBundle.KEY_COMPOSE_ENABLED))
            }
        }
    }
}