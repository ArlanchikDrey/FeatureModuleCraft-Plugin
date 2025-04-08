package io.github.arlanchikdrey.plugins.featuremodulecraft.ext

import com.android.tools.idea.wizard.template.RecipeExecutor
import java.io.File

@Deprecated("Wizard will no longer be supported")
fun RecipeExecutor.saveFile(absolutePath: String, relative: String, content: String) {
    val path = absolutePath.replace("/java/", "/kotlin/")
    val file = File(path).resolve(relative)
    save(content, file)
}