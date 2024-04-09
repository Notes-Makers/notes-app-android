package com.notesmakers.ui.image

import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.Matrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.extensions.pxToDp

@Composable
fun ImageResizerView(
    isImageResizerView: Boolean,
    imageBitmap: ImageBitmap,
    offsetBefore: Offset,
    addNewBitmapWithOffset: (ImageBitmap, Offset) -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(offsetBefore) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    LaunchedEffect(offsetBefore) {
        offset = offsetBefore
    }
    val bitmapTransformedScaledImageBitmap by remember(scale) {
        mutableStateOf(imageBitmap.transformedScaledImageBitmap(scale))
    }
    if (isImageResizerView) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                bitmap = bitmapTransformedScaledImageBitmap
                    .asImageBitmap(),
                contentDescription = null,
                alignment = Alignment.TopStart,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .offset(
                        offset.x.pxToDp(), offset.y.pxToDp()
                    )
                    .transformable(state = state)
            )

        }
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(modifier = Modifier
                .offset(
                    offset.x.pxToDp() - 12.dp,
                    offset.y.pxToDp() - 12.dp
                )
                .background(
                    color = Color(0xff10B981).copy(alpha = 0.8f),
                    shape = CircleShape
                ),
                onClick = {
                    addNewBitmapWithOffset(
                        bitmapTransformedScaledImageBitmap
                            .asImageBitmap(), offset
                    )
                }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            }
        }
    }
}

fun ImageBitmap.transformedScaledImageBitmap(
    scale: Float
): Bitmap {
    val transformedWidth = (this.width * scale).toInt()
    val transformedHeight = (this.height * scale).toInt()
    return createScaledBitmap(this.asAndroidBitmap(), transformedWidth, transformedHeight, true)
}

//For Feature
private fun Bitmap.rotateBitmap(rotationDegrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(rotationDegrees)
    return Bitmap.createBitmap(
        /* source = */ this,
        /* x = */ 0,
        /* y = */ 0,
        /* width = */ this.width,
        /* height = */ this.height,
        /* m = */ matrix,
        /* filter = */ true
    )
}
