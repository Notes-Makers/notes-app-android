package com.notesmakers.ui.paint.models

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.notesmakers.ui.image.getResizedBitmap

class BitmapProperties(
    var width: Int,
    var height: Int,
    var scale: Float,
    var offset: Offset,
    val bitmap: Bitmap
) {
    val scaledBitmap = getResizedBitmap(bitmap = bitmap, scale = scale)
}

class TextProperties(
    var text: String,
    var color: Color,
    var offset: Offset,
)