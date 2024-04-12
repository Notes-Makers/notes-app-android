package com.notesmakers.ui.composables.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BaseWideButton(
    modifier: Modifier,
    label: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = label, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun BaseButton(
    modifier: Modifier,
    label: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .height(60.dp),
        onClick = onClick,
        shape = CircleShape
    ) {
        Text(text = label, fontSize = 20.sp, color = Color.White)
    }
}
