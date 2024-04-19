package com.notesmakers.noteapp.features.notes.presentation.paintnote.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp

class TextProperties(
    var text: String = "",
    var color: Color = Color.Black,
    var offset: Offset,
) : DrawableComponent {
    override fun paint(drawScope: DrawScope, textMeasurer: TextMeasurer?) {
        textMeasurer?.let {
            drawScope.drawText(
                textMeasurer = it,
                text = text,
                topLeft = offset,
                style = TextStyle(
                    color = color,
                    fontSize = 12.sp
                )
            )
        }
    }
}