package com.notesmakers.ui.paint.extensions

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.notesmakers.ui.paint.models.PathProperties

fun DrawScope.drawToolTrace(path: Path? = null, pathProperties: PathProperties) {
    if (pathProperties.eraseMode) {
        drawPath(
            color = Color.Transparent,
            path = path ?: pathProperties.path,
            style = Stroke(
                width = pathProperties.strokeWidth,
                cap = pathProperties.strokeCap,
                join = pathProperties.strokeJoin
            ),
            blendMode = BlendMode.Clear
        )
    } else {
        drawPath(
            color = pathProperties.color.copy(alpha = pathProperties.alpha),
            path = path ?: pathProperties.path,
            style = Stroke(
                width = pathProperties.strokeWidth,
                cap = pathProperties.strokeCap,
                join = pathProperties.strokeJoin
            )
        )
    }
}

fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}