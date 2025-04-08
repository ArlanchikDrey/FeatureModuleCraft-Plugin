package io.github.arlanchikdrey.plugins.featuremodulecraft.provider

import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.WizardTemplateProvider
import io.github.arlanchikdrey.plugins.featuremodulecraft.template.featureModuleTemplate

@Deprecated("Wizard will no longer be supported")
class FeatureModuleWizardTemplateProvider : WizardTemplateProvider() {
    override fun getTemplates(): List<Template> {
        return listOf(featureModuleTemplate)
    }
}