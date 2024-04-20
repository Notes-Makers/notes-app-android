package com.notesmakers.noteapp.extension

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.vector.PathParser

fun Path.getSvgPath(): String {
    val pathMeasure = PathMeasure().apply {
        setPath(this@getSvgPath, false)
    }
    val pathLength = pathMeasure.length
    val stringBuilder = StringBuilder()
    if (pathLength == 0f) {
        throw Exception("Nothing to write")
    }
    stringBuilder.append("M ${pathMeasure.getPosition(0f).x} ${pathMeasure.getPosition(0f).y} ")
    for (i in 1..pathLength.toInt()) {
        val offset = pathMeasure.getPosition(i.toFloat())
        stringBuilder.append("L ${offset.x} ${offset.y}")
    }
    return stringBuilder.toString()
}

fun String.toPath() = PathParser().parsePathString(this).toPath()