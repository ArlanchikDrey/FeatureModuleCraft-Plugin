package io.github.arlanchikdrey.plugins.featuremodulecraft.template

import com.android.tools.idea.wizard.template.*
import io.github.arlanchikdrey.plugins.featuremodulecraft.bundle.StringsBundle
import io.github.arlanchikdrey.plugins.featuremodulecraft.template.recipes.createModuleFiles

@Deprecated("Wizard will no longer be supported")
val featureModuleTemplate: Template
    get() = template {
        name = StringsBundle.get(StringsBundle.KEY_NAME)
        minApi = 21
        description = StringsBundle.get(StringsBundle.KEY_DESCRIPTION)

        category = Category.Other
        formFactor = FormFactor.Mobile
        useGenericAndroidTests = false
        useGenericLocalTests = false

        screens = listOf(
            WizardUiContext.NewModule,
            WizardUiContext.MenuEntry
        )

        val featureName = stringParameter {
            name = StringsBundle.get(StringsBundle.KEY_FEATURE_NAME)
            default = ""
        }

        val diFilesEnabled = booleanParameter {
            name = StringsBundle.get(StringsBundle.KEY_DI_FILES_ENABLED)
            default = false
        }

        val domainLayerEnabled = booleanParameter {
            name = StringsBundle.get(StringsBundle.KEY_DOMAIN_LAYER_ENABLED)
            default = false
        }

        val dataLayerEnabled = booleanParameter {
            name = StringsBundle.get(StringsBundle.KEY_DATA_LAYER_ENABLED)
            default = false
        }

        val presentationLayerEnabled = booleanParameter {
            name = StringsBundle.get(StringsBundle.KEY_PRESENTATION_LAYER_ENABLED)
            default = false
        }

        widgets(
            TextFieldWidget(featureName),
            Separator,
            CheckBoxWidget(domainLayerEnabled),
            CheckBoxWidget(dataLayerEnabled),
            CheckBoxWidget(presentationLayerEnabled),
            CheckBoxWidget(diFilesEnabled)
        )

        recipe = { data: TemplateData ->
            createModuleFiles(
                moduleTempData = data as ModuleTemplateData,
                featureName = featureName.value,
                diFilesEnabled = diFilesEnabled.value
            )
        }
    }