package com.notesmakers.noteapp.features.notes.presentation.paintnote.models

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.notesmakers.ui.image.getResizedBitmap

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
