package io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model

object GitIgnoreModel {
    fun get(): String {
        return """
        /build
    """.trimIndent()
    }
}