package com.notesmakers.ui.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint


fun getResizedBitmap(bitmap: Bitmap, scale: Float): Bitmap {
    val newWidth = (bitmap.width * scale).toInt()
    val newHeight = (bitmap.height * scale).toInt()

    val resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

    val scaleMatrix = Matrix()
    scaleMatrix.setScale(scale, scale)
    val canvas = Canvas(resizedBitmap)
    canvas.setMatrix(scaleMatrix)
    canvas.drawBitmap(
        bitmap.copy(Bitmap.Config.ARGB_8888, false),
        0f,
        0f,
        Paint(Paint.FILTER_BITMAP_FLAG).apply {
            isFilterBitmap = false
            isAntiAlias = true
        })
    return resizedBitmap
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
