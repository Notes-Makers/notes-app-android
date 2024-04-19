package com.notesmakers.noteapp.features.notes.presentation.paintnote.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.noteapp.features.notes.presentation.paintnote.menu.ColorSelector
import com.notesmakers.noteapp.features.notes.presentation.paintnote.models.TextProperties
import com.notesmakers.ui.theme.paintColors


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlainTextManipulator(
    textProperties: TextProperties,
    modifier: Modifier = Modifier,
    addNewText: (TextProperties) -> Unit,
    onDismiss: () -> Unit,
    onChange: (TextProperties) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(textProperties.text)) }
    var selectedColor by remember { mutableStateOf(textProperties.color) }
    var offset by remember { mutableStateOf(textProperties.offset) }

    LaunchedEffect(text, selectedColor, offset) {
        onChange(
            TextProperties(
                text = text.text,
                color = selectedColor,
                offset = offset
            )
        )
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Transparent)
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, _, _ ->
                offset += (pan * 0.5f)
            }
        }) {
        Card(
            modifier = modifier
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
            Column(modifier = Modifier.padding(12.dp)) {
                Row {
                    BaseIconButton(
                        onClick = {
                            addNewText(
                                TextProperties(
                                    text = text.text,
                                    color = selectedColor,
                                    offset = offset
                                )
                            )
                        },
                        modifier = Modifier.background(
                            color = Color(0xff10B981).copy(alpha = 0.8f),
                            shape = CircleShape
                        ),
                        imageVector = Icons.Default.Check,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.padding(end = 4.dp))
                    BaseIconButton(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier.background(
                            color = Color(0xffF43F5E).copy(alpha = 0.8f),
                            shape = CircleShape
                        ),
                        imageVector = Icons.Default.Clear,
                        tint = Color.White
                    )
                }

                OutlinedTextField(
                    value = text,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = selectedColor),
                    label = { Text(text = "Enter Your text") },
                    onValueChange = {
                        text = it
                    }
                )
                FlowRow(
                    modifier = Modifier.padding(vertical = 6.dp),
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
                            selectedColor = it
                        }
                    }
                }
            }
        }
    }
}
