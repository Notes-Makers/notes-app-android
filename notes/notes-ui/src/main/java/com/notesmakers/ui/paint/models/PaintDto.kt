package com.notesmakers.ui.paint.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class BitmapProperties(
    var width: Int,
    var height: Int,
    var offset: Offset,
)

class TextProperties(
    var text: String,
    var color: Color,
    var offset: Offset,
)