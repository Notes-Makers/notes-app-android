package com.notesmakers.ui.paintnote.models

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import com.notesmakers.ui.image.getResizedBitmap
import com.notesmakers.ui.paintnote.extensions.drawToolTrace

interface DrawableComponent {
    fun paint(drawScope: DrawScope, textMeasurer: TextMeasurer?)
}

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

class BitmapProperties(
    var width: Int,
    var height: Int,
    var scale: Float,
    var offset: Offset,
    val bitmap: Bitmap,
) : DrawableComponent {
    val scaledBitmap = getResizedBitmap(bitmap = bitmap, scale = scale)
    override fun paint(drawScope: DrawScope, textMeasurer: TextMeasurer?) {
        drawScope.drawImage(scaledBitmap.asImageBitmap(), offset)
    }
}

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