package io.github.arlanchikdrey.plugins.featuremodulecraft.bundle

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE_STRINGS = "messages.strings"

object StringsBundle : DynamicBundle(BUNDLE_STRINGS) {

    const val KEY_NAME = "template.name"
    const val KEY_DESCRIPTION = "template.description"

    const val KEY_FEATURE_NAME = "template.featureName"
    const val KEY_PACKAGE_NAME = "template.packageName"
    const val KEY_DI_FILES_ENABLED = "template.diFilesEnabled"
    const val KEY_DOMAIN_LAYER_ENABLED = "template.domainLayerEnabled"
    const val KEY_DATA_LAYER_ENABLED = "template.dataLayerEnabled"
    const val KEY_PRESENTATION_LAYER_ENABLED = "template.presentationLayerEnabled"

    const val KEY_EMPTY_FIELD = "validate.emptyField"
    const val KEY_FOLDER_ALREADY_EXISTS = "validate.folderAlreadyExists"

    @Nls
    fun get(
        @PropertyKey(resourceBundle = BUNDLE_STRINGS)
        key: String,
        vararg params: Any
    ): String = getMessage(key, params)
}