package io.github.arlanchikdrey.plugins.featuremodulecraft.domain.usecase

import com.intellij.openapi.vfs.VirtualFile

class FeatureNameValidatorUseCase {
    operator fun invoke(parentFolder: VirtualFile, name: String): ValidatorResult {
        var isFeatureAlreadyExists = false

        for (child in parentFolder.children) {
            if (name == child.name && child.isDirectory) {
                isFeatureAlreadyExists = true
                break
            }
        }

        return when {
            isFeatureAlreadyExists -> ValidatorResult.FeatureAlreadyExists
            name.isBlank() -> ValidatorResult.Empty
            else -> ValidatorResult.Success
        }
    }

    enum class ValidatorResult {
        FeatureAlreadyExists, Empty, Success
    }
}