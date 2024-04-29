package com.notesmakers.noteapp.presentation.notes.paintnote.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import com.notesmakers.noteapp.extension.decodeImage
import com.notesmakers.noteapp.extension.toColorInt
import com.notesmakers.noteapp.extension.toPath
import com.notesmakers.noteapp.data.notes.local.BitmapDrawable
import com.notesmakers.noteapp.data.notes.local.Drawable
import com.notesmakers.noteapp.data.notes.local.PathDrawable
import com.notesmakers.noteapp.data.notes.local.TextDrawable

interface DrawableComponent {
    fun paint(drawScope: DrawScope, textMeasurer: TextMeasurer?)
}

val String.color
    get() = Color(android.graphics.Color.parseColor(this))

fun List<Drawable>.toDrawableComponent() = map {
    when (it) {
        is PathDrawable -> {
            PathProperties(
                strokeWidth = it.strokeWidth,
                color = Color(it.color.toColorInt()),
                alpha = it.alpha,
                eraseMode = it.eraseMode,
                path = it.path.toPath()
            )
        }

        is TextDrawable -> {
            TextProperties(
                text = it.text,
                color = Color(it.color.toColorInt()),
                offset = Offset(it.offsetX, it.offsetY)
            )
        }

        is BitmapDrawable -> {
            BitmapProperties(
                width = it.width,
                height = it.height, scale = it.scale,
                offset = Offset(it.offsetX, it.offsetY),
                bitmap = it.bitmap.decodeImage()
            )
        }

        else -> {
            null
        }
    }
}.filterNotNull()
