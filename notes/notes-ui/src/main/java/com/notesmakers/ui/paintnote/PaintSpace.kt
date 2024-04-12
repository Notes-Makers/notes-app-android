package com.notesmakers.ui.paintnote

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notesmakers.ui.paintnote.components.BitmapManipulator
import com.notesmakers.ui.paintnote.components.PlainTextManipulator
import com.notesmakers.ui.paintnote.extensions.drawToolTrace
import com.notesmakers.ui.paintnote.extensions.drawWithLayer
import com.notesmakers.ui.paintnote.interaction.dragMotionEvent
import com.notesmakers.ui.paintnote.menu.PropertiesMenu
import com.notesmakers.ui.paintnote.models.BitmapProperties
import com.notesmakers.ui.paintnote.models.DrawableComponent
import com.notesmakers.ui.paintnote.models.MotionEvent
import com.notesmakers.ui.paintnote.models.PaintMode
import com.notesmakers.ui.paintnote.models.PathProperties
import com.notesmakers.ui.paintnote.models.TextProperties
import com.notesmakers.ui.paintnote.models.handleMotionEvent

@Composable
fun PaintSpace(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f))

    ) {
        var isMenuVisible by remember {
            mutableStateOf(false)
        }
        var longPressOffsetPosition by remember {
            mutableStateOf(Offset.Zero)
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

        //Path + Bitmap + Text
        val drawableComponents = remember { mutableStateListOf<DrawableComponent>() }

        var previewBitmap by remember {
            mutableStateOf<BitmapProperties?>(null)
        }
        var previewText by remember {
            mutableStateOf<TextProperties?>(null)
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
                drawableComponents = drawableComponents,
                previewText = previewText,
                tmpBitmap = previewBitmap,
                currentPathProperty = currentPathProperty,
                previousPosition = previousPosition,
                setMotionEvent = { motionEvent = it },
                setCurrentPosition = { currentPosition = it },
                setPreviousPosition = { previousPosition = it },
                setCurrentPathProperty = { currentPathProperty = it },
                setCurrentPath = { currentPath = it },
                addPaths = {
                    drawableComponents.add(it)
                },
                setLongPressPositionOffset = {
                    isMenuVisible = true
                    longPressOffsetPosition = it
                }
            )
            previewBitmap?.let { bitmapProperties ->
                BitmapManipulator(
                    bitmapProperties = bitmapProperties,
                    onAddBitmap = {
                        drawableComponents.add(it)
                        previewBitmap = null
                    },
                    onDismiss = { previewBitmap = null },
                    onChange = { previewBitmap = it }
                )
            }
        }
        previewText?.let { textProperties ->
            PlainTextManipulator(
                modifier = Modifier.align(Alignment.TopCenter),
                textProperties = textProperties,
                addNewText = {
                    drawableComponents.add(it)
                    previewText = null
                },
                onChange = {
                    previewText = it
                },
                onDismiss = {
                    previewText = null
                },
            )
        }
        PropertiesMenu(
            modifier = Modifier.align(Alignment.BottomCenter),
            pathProperties = currentPathProperty,
            paintMode = paintMode,
            isMenuVisible = isMenuVisible,
            longPressOffsetPosition = longPressOffsetPosition,
            onBitmapSet = { bitmapProperties ->
                isMenuVisible = false
                longPressOffsetPosition = Offset.Zero
                previewBitmap = bitmapProperties
            },
            setPaintMode = {
                previewBitmap = null
                previewText = null
                isMenuVisible = false
                longPressOffsetPosition = Offset.Zero
                paintMode = it
                currentPathProperty.eraseMode = (paintMode == PaintMode.Erase)
            },
            onTextSet = {
                previewText = it
                isMenuVisible = false
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
    drawableComponents: List<DrawableComponent>,
    previewText: TextProperties?,
    tmpBitmap: BitmapProperties?,
    motionEvent: MotionEvent,
    currentPosition: Offset,
    previousPosition: Offset,
    currentPathProperty: PathProperties,
    setMotionEvent: (MotionEvent) -> Unit,
    setCurrentPosition: (Offset) -> Unit,
    setPreviousPosition: (Offset) -> Unit,
    setCurrentPathProperty: (PathProperties) -> Unit,
    setCurrentPath: (Path) -> Unit,
    addPaths: (PathProperties) -> Unit,
    setLongPressPositionOffset: (Offset) -> Unit,
) {
    val textMeasure = rememberTextMeasurer(cacheSize = 0)
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
                    setLongPressPositionOffset(it)
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
                addPaths(
                    PathProperties(
                        strokeWidth = currentPathProperty.strokeWidth,
                        color = currentPathProperty.color,
                        strokeCap = currentPathProperty.strokeCap,
                        strokeJoin = currentPathProperty.strokeJoin,
                        eraseMode = currentPathProperty.eraseMode,
                        path = currentPath
                    )
                )
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
            drawableComponents.forEach {
                it.paint(this, textMeasure)
            }
            if (motionEvent != MotionEvent.Idle) {
                drawToolTrace(path = currentPath, pathProperties = currentPathProperty)
            }
            previewText?.let {
                drawText(
                    textMeasurer = textMeasure,
                    text = it.text,
                    topLeft = Offset(it.offset.x, it.offset.y),
                    style = TextStyle(
                        color = it.color,
                        fontSize = 12.sp
                    )
                )
            }
            tmpBitmap?.let {
                drawImage(it.scaledBitmap.asImageBitmap(), it.offset)
            }
        }
    }

}