package com.notesmakers.ui.paint

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.image.ImageResizerView
import com.notesmakers.ui.paint.extensions.drawToolTrace
import com.notesmakers.ui.paint.extensions.drawWithLayer
import com.notesmakers.ui.paint.interaction.dragMotionEvent
import com.notesmakers.ui.paint.menu.PropertiesMenu
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
        var contextPlaceMenu by remember {
            mutableStateOf(Pair(first = false, second = Offset.Zero))
        }

        var initScale by remember { mutableFloatStateOf(0f) }
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

        //Canvas Variable
        var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
        var currentPathProperty by remember { mutableStateOf(PathProperties()) }
        var currentPath by remember { mutableStateOf(Path()) }
        var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
        val paths = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
        //Bitmaps
        val bitmaps = remember { mutableStateListOf<Pair<Bitmap, Offset>>() }

        var tmpBitmap by remember {
            mutableStateOf<Pair<Bitmap, Offset>?>(null)
        }

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
            .transformable(state = state, enabled = paintMode == PaintMode.Transform)
            .onGloballyPositioned { childrenBox ->
                if (scale == 0f) {
                    val (widthMaxScale, heightMaxScale) = childrenBox.parentLayoutCoordinates?.let { parent ->
                        parent.size.width.toFloat() / childrenBox.size.width.toFloat() to parent.size.height.toFloat() / childrenBox.size.height.toFloat()
                    } ?: (0f to 0f)
                    if (widthMaxScale != 0f && heightMaxScale != 0f) {
                        scale = minOf(widthMaxScale, heightMaxScale)
                        initScale = minOf(widthMaxScale, heightMaxScale)
                    }
                }
            }) {
            PaperLayout(
                paintMode = paintMode,
                motionEvent = motionEvent,
                currentPath = currentPath,
                currentPosition = currentPosition,
                paths = paths,
                bitmaps = bitmaps,
                currentPathProperty = currentPathProperty,
                previousPosition = previousPosition,
                setMotionEvent = { motionEvent = it },
                setCurrentPosition = { currentPosition = it },
                setPreviousPosition = { previousPosition = it },
                setCurrentPathProperty = { currentPathProperty = it },
                setCurrentPath = { currentPath = it },
                addPaths = { paths.add(Pair(it.first, it.second)) },
                setContextPlaceMenu = {
                    contextPlaceMenu = it
                }
            )
            tmpBitmap?.let {
                ImageResizerView(
                    isImageResizerView = true,
                    imageBitmap = it.first.asImageBitmap(),
                    offsetBefore = it.second,
                    addNewBitmapWithOffset = { bitmap, currentOffset ->
                        bitmaps.add(Pair(bitmap.asAndroidBitmap(), currentOffset))
                        contextPlaceMenu = Pair(first = false, second = Offset.Zero)
                        tmpBitmap = null
                    }
                )
            }
        }
        PropertiesMenu(
            modifier = Modifier.align(Alignment.BottomCenter),
            pathProperties = currentPathProperty,
            paintMode = paintMode,
            contextPlaceMenu = contextPlaceMenu,
            onBitmapSet = { bitmap, currentOffset ->
                contextPlaceMenu = Pair(first = false, second = Offset.Zero)
                tmpBitmap = Pair(bitmap, currentOffset)
            },
            setPaintMode = {
                contextPlaceMenu = Pair(first = false, second = Offset.Zero)
                paintMode = it
                currentPathProperty.eraseMode = (paintMode == PaintMode.Erase)
            },
            resetPosition = {
                scale = initScale
                rotation = 0f
                offset = Offset.Zero
            })
    }
}

@Composable
fun PaperLayout(
    paintMode: PaintMode,
    currentPath: Path,
    paths: List<Pair<Path, PathProperties>>,
    bitmaps: List<Pair<Bitmap, Offset>>,
    motionEvent: MotionEvent,
    currentPosition: Offset,
    previousPosition: Offset,
    currentPathProperty: PathProperties,
    setMotionEvent: (MotionEvent) -> Unit,
    setCurrentPosition: (Offset) -> Unit,
    setPreviousPosition: (Offset) -> Unit,
    setCurrentPathProperty: (PathProperties) -> Unit,
    setCurrentPath: (Path) -> Unit,
    addPaths: (Pair<Path, PathProperties>) -> Unit,
    setContextPlaceMenu: (Pair<Boolean, Offset>) -> Unit,
) {

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .background(Color.White)
            .dragMotionEvent(
                paintMode = paintMode,
                onDragStart = {
                    setMotionEvent(MotionEvent.Down)
                    setCurrentPosition(it.position)
                    it.consume()
                },
                onDrag = {
                    setMotionEvent(MotionEvent.Move)
                    setCurrentPosition(it.position)
                    it.consume()

                },
                onDragEnd = {
                    setMotionEvent(MotionEvent.Up)
                    it.consume()
                },
                onLongPress = {
                    setContextPlaceMenu(Pair(true, it))
                }
            )
    ) {
        motionEvent.handleMotionEvent(
            onIdle = {},
            onDown = {
                currentPath.moveTo(currentPosition.x, currentPosition.y)
                setPreviousPosition(currentPosition)
            },
            onMove = {
                currentPath.quadraticBezierTo(
                    previousPosition.x,
                    previousPosition.y,
                    (previousPosition.x + currentPosition.x) / 2,
                    (previousPosition.y + currentPosition.y) / 2

                )
                setPreviousPosition(currentPosition)
            },
            onUp = {
                currentPath.lineTo(currentPosition.x, currentPosition.y)
                addPaths(Pair(currentPath, currentPathProperty))
                setCurrentPath(Path())
                setCurrentPathProperty(
                    PathProperties(
                        strokeWidth = currentPathProperty.strokeWidth,
                        color = currentPathProperty.color,
                        strokeCap = currentPathProperty.strokeCap,
                        strokeJoin = currentPathProperty.strokeJoin,
                        eraseMode = currentPathProperty.eraseMode
                    )
                )
                setCurrentPosition(Offset.Unspecified)
                setPreviousPosition(currentPosition)
                setMotionEvent(MotionEvent.Idle)
            },
        )
        drawWithLayer {
            bitmaps.forEach {
                drawImage(it.first.asImageBitmap(), it.second)
            }
            paths.forEach {
                drawToolTrace(
                    path = it.first,
                    pathProperties = it.second
                )
            } //Without this can showing item can do
            if (motionEvent != MotionEvent.Idle) {
                drawToolTrace(path = currentPath, pathProperties = currentPathProperty)
            }
        }
    }

}

inline val Float.dp: Dp
    @Composable get() = with(LocalDensity.current) { this@dp.toDp() }