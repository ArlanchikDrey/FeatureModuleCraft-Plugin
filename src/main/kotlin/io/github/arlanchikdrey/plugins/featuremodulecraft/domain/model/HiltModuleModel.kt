package io.github.arlanchikdrey.plugins.featuremodulecraft.domain.model

object HiltModuleModel {
    fun get(
        packageName: String,
        className: String
    ): String {
        return """
        package $packageName

        import dagger.Module
        import dagger.hilt.InstallIn
        import dagger.hilt.components.SingletonComponent
        
        @Module
        @InstallIn(SingletonComponent::class)
        internal interface $className {

        }
    """.trimIndent()
    }
}