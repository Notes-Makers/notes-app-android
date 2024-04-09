package com.notesmakers.ui.paint.menu

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.R
import com.notesmakers.ui.composables.ChipItem
import com.notesmakers.ui.composables.SelectedChipItem
import com.notesmakers.ui.image.PhotoSelectorView
import com.notesmakers.ui.paint.models.PaintMode
import com.notesmakers.ui.paint.models.PathProperties
import com.notesmakers.ui.theme.paintColors
import com.notesmakers.ui.theme.sliderDefaultColors


@Composable
fun PropertiesMenu(
    modifier: Modifier,
    pathProperties: PathProperties,
    paintMode: PaintMode,
    contextPlaceMenu: Pair<Boolean, Offset>,
    setPaintMode: (PaintMode) -> Unit,
    onBitmapSet: (Bitmap, Offset) -> Unit,
    resetPosition: () -> Unit,
) {
    val properties by rememberUpdatedState(newValue = pathProperties)

    var showExpandedPencilProperties by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlaceableToolMenu(
            contextPlaceMenu = contextPlaceMenu,
            onBitmapSet = onBitmapSet,
        )
        PaintModeMenu(
            pathProperties = properties,
            paintMode = paintMode,
            showExpandedPencilProperties = showExpandedPencilProperties,
            onExpandedPencilProperties = { showExpandedPencilProperties = it }
        )
        ToolMenu(
            paintMode = paintMode,
            setPaintMode = setPaintMode,
            resetPosition = resetPosition
        )
    }
}

@Composable
fun PlaceableToolMenu(
    onBitmapSet: (Bitmap, Offset) -> Unit,
    contextPlaceMenu: Pair<Boolean, Offset>,
) {
    AnimatedVisibility(visible = contextPlaceMenu.first) {
        Row(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { }
                    .padding(4.dp)) {
                Text(text = "Add Text")
                Icon(
                    painter = painterResource(R.drawable.text),
                    tint = LocalContentColor.current,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(24.dp),
                    contentDescription = null
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { }
                    .padding(4.dp)) {
                PhotoSelectorView(
                    content = { Text(text = "Add Image", modifier = it) },
                    onImageSelected = { bitmap ->
                        bitmap?.let { onBitmapSet(it, contextPlaceMenu.second) }
                    })

                Icon(
                    painter = painterResource(R.drawable.image),
                    tint = LocalContentColor.current,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(24.dp),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ToolMenu(
    paintMode: PaintMode,
    setPaintMode: (PaintMode) -> Unit,
    resetPosition: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .shadow(
                elevation =
                3.dp, shape = RoundedCornerShape(10.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .border(
                width = Dp.Hairline,
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 8.dp)

    ) {
        SelectedChipItem(
            text = "",
            painterResource = R.drawable.transform,
            selected = paintMode == PaintMode.Transform
        ) {
            setPaintMode(PaintMode.Transform)
        }
        SelectedChipItem(
            text = "",
            painterResource = R.drawable.long_press,
            selected = paintMode == PaintMode.Placeable
        ) {
            setPaintMode(PaintMode.Placeable)
        }
        SelectedChipItem(
            text = "",
            painterResource = R.drawable.pencil,
            selected = paintMode == PaintMode.Draw
        ) {
            setPaintMode(PaintMode.Draw)
        }
        SelectedChipItem(
            text = "",
            painterResource = R.drawable.erase,
            selected = paintMode == PaintMode.Erase
        ) {
            setPaintMode(PaintMode.Erase)
        }
        ChipItem(text = "Center", onClick = resetPosition)
    }
}

@Composable
private fun ColumnScope.PaintModeMenu(
    pathProperties: PathProperties,
    paintMode: PaintMode,
    showExpandedPencilProperties: Boolean,
    onExpandedPencilProperties: (Boolean) -> Unit
) {
    var strokeWidth by remember { mutableFloatStateOf(pathProperties.strokeWidth) }
    var selectedColor by remember { mutableStateOf(pathProperties.color) }

    if (paintMode == PaintMode.Erase || paintMode == PaintMode.Draw) {
        Row(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable { onExpandedPencilProperties(!showExpandedPencilProperties) }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)

        ) {
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (paintMode == PaintMode.Draw) selectedColor else Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = Dp.Hairline,
                        color = Color.LightGray,
                        shape = CircleShape
                    )
            )
            Text(text = "${strokeWidth.toInt()} width", fontWeight = FontWeight.Light)
            if (showExpandedPencilProperties) {
                Icon(
                    painter = painterResource(R.drawable.double_down),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        AnimatedVisibility(visible = showExpandedPencilProperties) {
            CardWithControls(
                paintMode = paintMode,
                strokeColor = selectedColor,
                strokeWidth = strokeWidth,
                updateStrokeColor = {
                    selectedColor = it
                    pathProperties.color = it
                },
                updateStrokeWidth = {
                    strokeWidth = it
                    pathProperties.strokeWidth = it
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardWithControls(
    strokeColor: Color,
    strokeWidth: Float,
    paintMode: PaintMode,
    updateStrokeColor: (Color) -> Unit,
    updateStrokeWidth: (Float) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .width(350.dp)
            .border(
                width = Dp.Hairline,
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Size", fontWeight = FontWeight.Light)
            Slider(
                value = strokeWidth,
                onValueChange = {
                    updateStrokeWidth(it)
                },
                valueRange = 5f..40f,
                steps = 35,
                colors = sliderDefaultColors()
            )
            if (paintMode != PaintMode.Erase) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Colors", fontWeight = FontWeight.Light)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    paintColors.forEach { color ->
                        ColorSelector(
                            color = color,
                        ) {
                            updateStrokeColor(color)
                        }
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Preview", fontWeight = FontWeight.Light)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    drawLine(
                        color = strokeColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSelector(color: Color, onClick: (Color) -> Unit) {

    Box(
        modifier = Modifier
            .size(35.dp)
            .clip(CircleShape)
            .clickable(onClick = { onClick(color) }),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color, shape = CircleShape)
                .border(width = Dp.Hairline, shape = CircleShape, color = Color.LightGray)
                .padding(4.dp)

        )
    }
}
