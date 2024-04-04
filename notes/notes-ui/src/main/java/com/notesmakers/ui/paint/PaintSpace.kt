package com.notesmakers.ui.paint

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.Sample.DrawMode
import com.notesmakers.ui.composables.ChipItem
import com.notesmakers.ui.composables.SelectedChipItem
import com.notesmakers.ui.paint.extensions.drawToolTrace
import com.notesmakers.ui.paint.extensions.drawWithLayer
import com.notesmakers.ui.paint.interaction.dragMotionEvent
import com.notesmakers.ui.paint.models.MotionEvent
import com.notesmakers.ui.paint.models.PaintMode
import com.notesmakers.ui.paint.models.PathProperties
import com.notesmakers.ui.paint.models.handleMotionEvent

@Composable
fun PaintSpace(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp)
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f))

    ) {
        //Transformable item section needed in transform mode
        var scale by remember { mutableFloatStateOf(0f) }
        var rotation by remember { mutableFloatStateOf(0f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale *= zoomChange
            rotation += rotationChange
            offset += offsetChange
        }
        //This is created because we not transform canvas but only box who is his contanier
        var paintMode by remember { mutableStateOf(PaintMode.Transform) }
        var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }

        Box(modifier = Modifier
            .size(
                width = 210.dp, height = 297.dp
            )//A4 paper Size
            .align(Alignment.Center)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y
            )
            .background(Color.Red)
            .transformable(state = state, enabled = paintMode == PaintMode.Transform)
            .onGloballyPositioned { childrenBox ->
                if (scale == 0f) {
                    val (widthMaxScale, heightMaxScale) = childrenBox.parentLayoutCoordinates?.let { parent ->
                        parent.size.width.toFloat() / childrenBox.size.width.toFloat() to parent.size.height.toFloat() / childrenBox.size.height.toFloat()
                    } ?: (0f to 0f)
                    if (widthMaxScale != 0f && heightMaxScale != 0f) {
                        scale = minOf(widthMaxScale, heightMaxScale)
                    }
                }
            }) {
            PaperLayout(
                paintMode = paintMode,
                motionEvent = motionEvent
            ) { motionEvent = it }
        }
        DrawMenu(modifier = Modifier.align(Alignment.BottomCenter),
            paintMode = paintMode,
            setPaintMode = { paintMode = it },
            resetPosition = {
                scale = 1f
                rotation = 0f
                offset = Offset.Zero
            })
    }
}

@Composable
private fun DrawMenu(
    modifier: Modifier,
    paintMode: PaintMode,
    setPaintMode: (PaintMode) -> Unit,
    resetPosition: () -> Unit,
) {
    Row(modifier = modifier) {
        SelectedChipItem(text = "Transform", selected = paintMode == PaintMode.Transform) {
            setPaintMode(PaintMode.Transform)
        }
        SelectedChipItem(text = "Draw", selected = paintMode == PaintMode.Draw) {
            setPaintMode(PaintMode.Draw)
        }
        SelectedChipItem(text = "Erase", selected = paintMode == PaintMode.Erase) {
            setPaintMode(PaintMode.Erase)
        }
        ChipItem(text = "Clear", onClick = resetPosition)
    }
}

@Composable
fun PaperLayout(
    paintMode: PaintMode,
//    setPaintMode: (PaintMode) -> Unit,
    motionEvent: MotionEvent,
    setMotionEvent: (MotionEvent) -> Unit
) {
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var currentPath by remember { mutableStateOf(Path()) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }

    val paths = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
    var currentPathProperty by remember { mutableStateOf(PathProperties()) }
    LaunchedEffect(paintMode) {
        setMotionEvent(MotionEvent.Idle)
        currentPathProperty.eraseMode = (paintMode == PaintMode.Erase)
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .background(Color.White)
            .dragMotionEvent(
                enabled = paintMode != PaintMode.Transform,
                onDragStart = {
                    setMotionEvent(MotionEvent.Down)
                    currentPosition = it.position
                    it.consume()
                },
                onDrag = {
                    setMotionEvent(MotionEvent.Move)
                    currentPosition = it.position
                    it.consume()

                },
                onDragEnd = {
                    setMotionEvent(MotionEvent.Up)
                    it.consume()
                },
            )
    ) {
        motionEvent.handleMotionEvent(
            onIdle = {},
            onDown = {
                currentPath.moveTo(currentPosition.x, currentPosition.y)
                previousPosition = currentPosition
            },
            onMove = {
                currentPath.quadraticBezierTo(
                    previousPosition.x,
                    previousPosition.y,
                    (previousPosition.x + currentPosition.x) / 2,
                    (previousPosition.y + currentPosition.y) / 2

                )
                previousPosition = currentPosition
            },
            onUp = {
                currentPath.lineTo(currentPosition.x, currentPosition.y)
                paths.add(Pair(currentPath, currentPathProperty))
                currentPath = Path()
                currentPathProperty = PathProperties(
                    strokeWidth = currentPathProperty.strokeWidth,
                    color = currentPathProperty.color,
                    strokeCap = currentPathProperty.strokeCap,
                    strokeJoin = currentPathProperty.strokeJoin,
                    eraseMode = currentPathProperty.eraseMode
                )

                currentPosition = Offset.Unspecified
                previousPosition = currentPosition
                setMotionEvent(MotionEvent.Idle)
            },
        )

        drawWithLayer {
            paths.forEach { drawToolTrace(path = it.first, pathProperties = it.second) }
            if (motionEvent != MotionEvent.Idle) {
                drawToolTrace(path = currentPath, pathProperties = currentPathProperty)
            }
        }
    }
}