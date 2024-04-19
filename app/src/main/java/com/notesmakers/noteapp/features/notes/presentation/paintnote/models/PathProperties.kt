package com.notesmakers.noteapp.features.notes.presentation.paintnote.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.notesmakers.noteapp.features.notes.presentation.paintnote.extensions.drawToolTrace

class PathProperties(
    var strokeWidth: Float = 5f,
    var color: Color = Color(0xFF3B82F6),
    var alpha: Float = 1f,
    var strokeCap: StrokeCap = StrokeCap.Round,
    var strokeJoin: StrokeJoin = StrokeJoin.Round,
    var eraseMode: Boolean = false,
    var path: Path = Path(),
) : DrawableComponent {
    override fun paint(drawScope: DrawScope, textMeasurer: TextMeasurer?) {
        drawScope.drawToolTrace(
            pathProperties = this
        )
    }
}