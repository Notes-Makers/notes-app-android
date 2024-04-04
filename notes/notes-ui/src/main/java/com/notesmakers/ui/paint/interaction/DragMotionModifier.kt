package com.notesmakers.ui.paint.interaction

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.dragMotionEvent(
    enabled: Boolean,
    onDragStart: (PointerInputChange) -> Unit = {},
    onDrag: (PointerInputChange) -> Unit = {},
    onDragEnd: (PointerInputChange) -> Unit = {}
) = this.then(
    if (enabled) {
        Modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitDragMotionEvent(onDragStart, onDrag, onDragEnd)
            }
        }
    } else Modifier
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