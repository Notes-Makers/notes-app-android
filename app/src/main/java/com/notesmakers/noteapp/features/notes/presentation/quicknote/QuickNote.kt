package com.notesmakers.noteapp.features.notes.presentation.quicknote

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.notesmakers.ui.composables.buttons.BaseButton
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.composables.inputs.BaseTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuickNote(modifier: Modifier) {
    val state = rememberRichTextState()

    var linkSelected by rememberSaveable { mutableStateOf(false) }

    var showLinkDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = showLinkDialog) {
        LinkDialog(
            onDismissRequest = {
                showLinkDialog = false
                linkSelected = false
            },
            onConfirmation = { linkText, link ->
                state.addLink(
                    text = linkText,
                    url = link
                )
                showLinkDialog = false
                linkSelected = false
            }
        )

    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {

        FlowRow(
            modifier = modifier
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
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BaseIconButton(
                onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) },
                painterResource = com.notesmakers.common_ui.R.drawable.bold
            )
            BaseIconButton(
                onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) },
                painterResource = com.notesmakers.common_ui.R.drawable.italic
            )
            BaseIconButton(
                onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) },
                painterResource = com.notesmakers.common_ui.R.drawable.underline
            )
            BaseIconButton(
                onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start)) },
                painterResource = com.notesmakers.common_ui.R.drawable.align_left
            )
            BaseIconButton(
                onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) },
                painterResource = com.notesmakers.common_ui.R.drawable.align_justify
            )
            BaseIconButton(
                onClick = { state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End)) },
                painterResource = com.notesmakers.common_ui.R.drawable.align_right
            )
            BaseIconButton(
                onClick = { showLinkDialog = true },
                painterResource = com.notesmakers.common_ui.R.drawable.add_link
            )
        }
        RichTextEditor(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            state = state,
            colors = RichTextEditorDefaults.richTextEditorColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
    }
}

@Composable
fun LinkDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var linkText by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BaseTextField(
                onValueChange = { newText -> linkText = newText },
                labelText = "Enter text link",
                placeholderText = "Text link",
                focusManager = focusManager,
                errorMessage = null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onConfirmation(linkText, link)
                    }
                ),
            )
            BaseTextField(
                onValueChange = { newText -> link = newText },
                labelText = "Enter link",
                placeholderText = "Link",
                focusManager = focusManager,
                errorMessage = null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onConfirmation(linkText, link)
                    }
                ),
            )
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                BaseButton(modifier = Modifier, label = "Add link") {
                    onConfirmation(linkText, link)
                }
            }
        }
    }
}
