package com.notesmakers.noteapp.presentation.notes.paintnote.interaction

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import com.notesmakers.noteapp.presentation.notes.paintnote.models.PaintMode

fun Modifier.dragMotionEvent(
    paintMode: PaintMode,
    onDragStart: (PointerInputChange) -> Unit = {},
    onDrag: (PointerInputChange) -> Unit = {},
    onDragEnd: (PointerInputChange) -> Unit = {},
    onLongPress: (Offset) -> Unit = {},
) = this.then(
    when (paintMode) {
        PaintMode.Transform -> Modifier
        PaintMode.Placeable -> Modifier.pointerInput(paintMode) {
            detectTapGestures(
                onLongPress = {
                    onLongPress(it)
                }
            )
        }

        PaintMode.Draw, PaintMode.Erase -> Modifier.pointerInput(paintMode) {
            awaitEachGesture {
                awaitDragMotionEvent(
                    onDragStart = onDragStart,
                    onDrag = onDrag,
                    onDragEnd = onDragEnd
                )
            }
        }
    }

)

private suspend fun AwaitPointerEventScope.awaitDragMotionEvent(
    onDragStart: (PointerInputChange) -> Unit = {},
    onDrag: (PointerInputChange) -> Unit = {},
    onDragEnd: (PointerInputChange) -> Unit = {}
) {
    val down: PointerInputChange = awaitFirstDown()
    onDragStart(down)

    var pointer = down
    val change: PointerInputChange? =
        awaitTouchSlopOrCancellation(down.id) { change: PointerInputChange, _: Offset ->
            change.consume()
        }

    if (change != null) {
        drag(change.id) { pointerInputChange: PointerInputChange ->
            pointer = pointerInputChange
            onDrag(pointer)
        }
        onDragEnd(pointer)
    } else {
        onDragEnd(pointer)
    }
}