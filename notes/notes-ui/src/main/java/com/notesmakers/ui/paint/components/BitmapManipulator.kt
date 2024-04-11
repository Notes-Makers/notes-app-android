package com.notesmakers.ui.paint.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.extensions.pxToDp
import com.notesmakers.ui.paint.models.BitmapProperties

@Composable
fun BitmapManipulator(
    bitmapProperties: BitmapProperties,
    onChange: (BitmapProperties) -> Unit,
    onAddBitmap: (BitmapProperties) -> Unit,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(bitmapProperties.scale) }
    var offset by remember { mutableStateOf(bitmapProperties.offset) }

    LaunchedEffect(offset, scale) {
        onChange(
            BitmapProperties(
                bitmapProperties.width,
                height = bitmapProperties.height,
                scale = scale,
                offset = offset,
                bitmap = bitmapProperties.bitmap
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    offset += pan
                    scale *= zoom.takeIf { it > 0 && it < 3f } ?: zoom
                }
            }
    ) {
    }
    ControlMenu(
        offset = offset,
        bitmapProperties = bitmapProperties,
        scale = scale,
        onAddBitmap = onAddBitmap,
        onDismiss = onDismiss
    )
}

@Composable
private fun ControlMenu(
    offset: Offset,
    bitmapProperties: BitmapProperties,
    scale: Float,
    onAddBitmap: (BitmapProperties) -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .offset(
                    x = offset.x.pxToDp() - 12.dp,
                    y = offset.y.pxToDp() - 12.dp
                )
        ) {
            IconButton(modifier = Modifier
                .background(
                    color = Color(0xff10B981).copy(alpha = 0.8f),
                    shape = CircleShape
                ),
                onClick = {
                    bitmapProperties.offset = offset
                    bitmapProperties.scale = scale
                    onAddBitmap(bitmapProperties)
                }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.padding(end = 4.dp))
            IconButton(modifier = Modifier
                .background(
                    color = Color(0xffF43F5E).copy(alpha = 0.8f),
                    shape = CircleShape
                ),
                onClick = {
                    onDismiss()
                }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
        }
    }
}


