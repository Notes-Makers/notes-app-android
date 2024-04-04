package com.notesmakers.ui

import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.Sample.model.PathProperties
import com.notesmakers.ui.gesture.MotionEvent
import com.notesmakers.ui.gesture.dragMotionEvent

@Composable
fun DrawSpace() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f))
            .padding(4.dp)
    ) {

        var drawMode by remember { mutableStateOf(DrawMode.Draw) }
        var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
        var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
        var currentPath by remember { mutableStateOf(Path()) }
        val paths = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
        var currentPathProperty by remember { mutableStateOf(PathProperties()) }
        val pathsUndone = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
        var previousPosition by remember { mutableStateOf(Offset.Unspecified) }

        var scale by remember { mutableStateOf(1f) }
        var rotation by remember { mutableStateOf(0f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            if (drawMode == DrawMode.Move) {
                scale *= zoomChange
                rotation += rotationChange
                offset += offsetChange
            }
        }
        val resources = LocalContext.current.resources
        val densityDpi = resources.displayMetrics.densityDpi
        val widthInInches = 2.5f
        val heightInInches = 3.5f

        // Obliczanie rozmiaru w pikselach na podstawie DPI urządzenia
        val widthInPixels = (widthInInches * densityDpi).dp
        val heightInPixels = (heightInInches * densityDpi).dp

        val drawModifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .size(width = widthInPixels, height = heightInPixels)
            .then(
                if (drawMode != DrawMode.Move) {
                    Modifier.dragMotionEvent(onDragStart = { pointerInputChange ->
                        motionEvent = MotionEvent.Down
                        currentPosition = pointerInputChange.position
                        pointerInputChange.consume()

                    }, onDrag = { pointerInputChange ->
                        motionEvent = MotionEvent.Move
                        currentPosition = pointerInputChange.position

                        if (drawMode == DrawMode.Touch) {
                            val change = pointerInputChange.positionChange()
                            println("DRAG: $change")
                            paths.forEach { entry ->
                                val path: Path = entry.first
                                path.translate(change)
                            }
                            currentPath.translate(change)
                        }
                        pointerInputChange.consume()

                    }, onDragEnd = { pointerInputChange ->
                        motionEvent = MotionEvent.Up
                        pointerInputChange.consume()
                    })
                } else Modifier
            )


        Box(
            modifier = Modifier
                .padding(2.dp)
                .shadow(1.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .shadow(1.dp)
                .background(color = MaterialTheme.colorScheme.onPrimary)
                .fillMaxSize()
                .then(
                    if (drawMode == DrawMode.Move) {
                        Modifier.transformable(state = state)
                    } else Modifier
                )
        ) {

            val image = remember { drawToBitmap() }

            // Zawartość Box
            Canvas(modifier = drawModifier) {

                drawImage(image)
                when (motionEvent) {

                    MotionEvent.Down -> {
                        if (drawMode != DrawMode.Touch) {
                            currentPath.moveTo(currentPosition.x, currentPosition.y)
                        }

                        previousPosition = currentPosition

                    }

                    MotionEvent.Move -> {

                        if (drawMode != DrawMode.Touch) {
                            currentPath.quadraticBezierTo(
                                previousPosition.x,
                                previousPosition.y,
                                (previousPosition.x + currentPosition.x) / 2,
                                (previousPosition.y + currentPosition.y) / 2

                            )
                        }

                        previousPosition = currentPosition
                    }

                    MotionEvent.Up -> {
                        if (drawMode != DrawMode.Touch) {
                            currentPath.lineTo(currentPosition.x, currentPosition.y)
                            // Pointer is up save current path
//                        paths[currentPath] = currentPathProperty
                            paths.add(Pair(currentPath, currentPathProperty))

                            // Since paths are keys for map, use new one for each key
                            // and have separate path for each down-move-up gesture cycle
                            currentPath = Path()

                            // Create new instance of path properties to have new path and properties
                            // only for the one currently being drawn
                            currentPathProperty = PathProperties(
                                strokeWidth = currentPathProperty.strokeWidth,
                                color = currentPathProperty.color,
                                strokeCap = currentPathProperty.strokeCap,
                                strokeJoin = currentPathProperty.strokeJoin,
                                eraseMode = currentPathProperty.eraseMode
                            )
                        }

                        // Since new path is drawn no need to store paths to undone
                        pathsUndone.clear()

                        // If we leave this state at MotionEvent.Up it causes current path to draw
                        // line from (0,0) if this composable recomposes when draw mode is changed
                        currentPosition = Offset.Unspecified
                        previousPosition = currentPosition
                        motionEvent = MotionEvent.Idle
                    }

                    else -> Unit
                }

                with(drawContext.canvas.nativeCanvas) {

                    val checkPoint = saveLayer(null, null)

                    paths.forEach {

                        val path = it.first
                        val property = it.second

                        if (!property.eraseMode) {
                            drawPath(
                                color = property.color,
                                path = path,
                                style = Stroke(
                                    width = property.strokeWidth,
                                    cap = property.strokeCap,
                                    join = property.strokeJoin
                                )
                            )
                        } else {

                            // Source
                            drawPath(
                                color = Color.Transparent,
                                path = path,
                                style = Stroke(
                                    width = currentPathProperty.strokeWidth,
                                    cap = currentPathProperty.strokeCap,
                                    join = currentPathProperty.strokeJoin
                                ),
                                blendMode = BlendMode.Clear
                            )
                        }
                    }

                    if (motionEvent != MotionEvent.Idle) {

                        if (!currentPathProperty.eraseMode) {
                            drawPath(
                                color = currentPathProperty.color,
                                path = currentPath,
                                style = Stroke(
                                    width = currentPathProperty.strokeWidth,
                                    cap = currentPathProperty.strokeCap,
                                    join = currentPathProperty.strokeJoin
                                )
                            )
                        } else {
                            drawPath(
                                color = Color.Transparent,
                                path = currentPath,
                                style = Stroke(
                                    width = currentPathProperty.strokeWidth,
                                    cap = currentPathProperty.strokeCap,
                                    join = currentPathProperty.strokeJoin
                                ),
                                blendMode = BlendMode.Clear
                            )
                        }
                    }
                    restoreToCount(checkPoint)
                }

//                drawScope = this
            }

        }

        Text(
            text = "sc: $scale ro:$rotation x:${offset.x} y${offset.y}", modifier = Modifier
                .offset(0.dp, 0.dp)
        )

        Row(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomCenter)
        ) {
            TextButton(onClick = { drawMode = DrawMode.Move }) {
                Text(text = "Move")
            }
            TextButton(onClick = { drawMode = DrawMode.Draw }) {
                Text(text = "Draw")
            }
            TextButton(onClick = { drawMode = DrawMode.Touch }) {
                Text(text = "Touch")
            }
            TextButton(onClick = { drawMode = DrawMode.Erase }) {
                Text(text = "Erase")
            }
            TextButton(onClick = {
//                val canvasDrawScope = CanvasDrawScope()
//                val size = Size(400f, 400f)
//                val bitmap = canvasDrawScope.asBitmap(size){
//                    this = drawScope
//                }

            }) {
                Text(text = "save")
            }
        }
    }
}

fun CanvasDrawScope.asBitmap(size: Size, onDraw: DrawScope.() -> Unit): ImageBitmap {
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    draw(Density(1f), LayoutDirection.Ltr, Canvas(bitmap), size) { onDraw() }
    return bitmap
}


fun drawToBitmap(): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val size = Size(400f, 400f)
    val bitmap = drawScope.asBitmap(size) {
        // Draw whatever you want here; for instance, a white background and a red line.
        drawRect(color = Color.White, topLeft = Offset.Zero, size = size)
        drawLine(
            color = Color.Red,
            start = Offset.Zero,
            end = Offset(size.width, size.height),
            strokeWidth = 5f
        )

    }

    return bitmap
}

enum class DrawMode {
    Move, Draw, Touch, Erase
}