package io.github.arlanchikdrey.plugins.featuremodulecraft.ext

fun String.appendIf(condition: Boolean): String = takeIf { condition }.orEmpty()