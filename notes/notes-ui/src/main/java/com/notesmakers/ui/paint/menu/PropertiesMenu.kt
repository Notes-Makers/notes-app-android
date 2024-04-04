package com.notesmakers.ui.paint.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.notesmakers.ui.paint.models.PaintMode
import com.notesmakers.ui.theme.paintColors


@Composable
fun PropertiesMenu(
    modifier: Modifier,
    paintMode: PaintMode,
    setPaintMode: (PaintMode) -> Unit,
    resetPosition: () -> Unit,
) {
    var showExpandedPencilProperties by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (paintMode == PaintMode.Erase || paintMode == PaintMode.Draw)
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { showExpandedPencilProperties = !showExpandedPencilProperties }
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)

            ) {
                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (paintMode == PaintMode.Draw) Color(0xFF3B82F6) else Color.White,
                            shape = CircleShape
                        )
                        .border(
                            width = Dp.Hairline,
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                )
                Text(text = "5 width", fontWeight = FontWeight.Light)
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
                paintMode = paintMode
            )
        }
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
                text = "Transform",
                painterResource = R.drawable.transform,
                selected = paintMode == PaintMode.Transform
            ) {
                setPaintMode(PaintMode.Transform)
            }
            SelectedChipItem(
                text = "Draw",
                painterResource = R.drawable.pencil,
                selected = paintMode == PaintMode.Draw
            ) {
                setPaintMode(PaintMode.Draw)
            }
            SelectedChipItem(
                text = "Erase",
                painterResource = R.drawable.erase,
                selected = paintMode == PaintMode.Erase
            ) {
                setPaintMode(PaintMode.Erase)
            }
            ChipItem(text = "Center", onClick = resetPosition)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardWithControls(
    paintMode: PaintMode,
) {
    var textSize by remember { mutableStateOf(16f) }
    var selectedColor by remember { mutableStateOf(Color.Red) }

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
                value = textSize,
                onValueChange = { textSize = it },
                valueRange = 8f..40f,
                steps = 32,
                colors = SliderColors(
                    thumbColor = Color(0xFF727272),
                    activeTrackColor = Color(0xFF727272),
                    activeTickColor = Color(0xFF727272),
                    inactiveTrackColor = Color(0xFF727272),
                    inactiveTickColor = Color(0xFF727272),
                    disabledThumbColor = Color(0xFF727272),
                    disabledActiveTrackColor = Color(0xFF727272),
                    disabledActiveTickColor = Color(0xFF727272),
                    disabledInactiveTrackColor = Color(0xFF727272),
                    disabledInactiveTickColor = Color(0xFF727272),
                )
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
                    paintColors.forEach {
                        ColorSelector(
                            color = it,
                        ) {
                            selectedColor = it
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
                        color = selectedColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = textSize,
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
                .padding(4.dp)

        )
    }
}
