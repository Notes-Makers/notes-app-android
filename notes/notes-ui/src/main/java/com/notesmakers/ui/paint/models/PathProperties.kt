package com.notesmakers.ui.paint.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin

class PathProperties(
    var strokeWidth: Float = 5f,
    var color: Color = Color(0xFF3B82F6),
    var alpha: Float = 1f,
    var strokeCap: StrokeCap = StrokeCap.Round,
    var strokeJoin: StrokeJoin = StrokeJoin.Round,
    var eraseMode: Boolean = false
)
