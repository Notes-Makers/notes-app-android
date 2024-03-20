package com.notesmakers.common_ui.buttons

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

@Composable
fun BaseIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes painterResource: Int,
    tint: Color = LocalContentColor.current,
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(painterResource),
            tint = tint,
            modifier = modifier,
            contentDescription = null
        )
    }
}

@Composable
fun BaseIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = LocalContentColor.current,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            tint = tint,
            modifier = modifier,
            contentDescription = null
        )
    }
}
