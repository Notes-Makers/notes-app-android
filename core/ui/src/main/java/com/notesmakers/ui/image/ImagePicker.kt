package com.notesmakers.ui.image

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun PhotoSelectorView(
    content: @Composable (Modifier) -> Unit,
    onImageSelected: (Bitmap?) -> Unit
) {

    val context = LocalContext.current
    var selectedImages by remember {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(key1 = selectedImages) {
        onImageSelected(selectedImages?.loadBitmapFromUri(context) )
    }


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImages =  uri
        }
    )

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    content(Modifier.clickable { launchPhotoPicker() })
}

private fun Uri.loadBitmapFromUri(context: android.content.Context): Bitmap {
    val source = ImageDecoder.createSource(context.contentResolver, this@loadBitmapFromUri)
    return ImageDecoder.decodeBitmap(source)
}