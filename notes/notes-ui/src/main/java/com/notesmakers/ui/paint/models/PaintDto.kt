package com.notesmakers.ui.paint.models

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import com.notesmakers.ui.image.getResizedBitmap

class PathProperties(
    var strokeWidth: Float = 5f,
    var color: Color = Color(0xFF3B82F6),
    var alpha: Float = 1f,
    var strokeCap: StrokeCap = StrokeCap.Round,
    var strokeJoin: StrokeJoin = StrokeJoin.Round,
    var eraseMode: Boolean = false,
    var path: Path = Path(),
)

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
    var text: String = "",
    var color: Color = Color.Black,
    var offset: Offset,
)