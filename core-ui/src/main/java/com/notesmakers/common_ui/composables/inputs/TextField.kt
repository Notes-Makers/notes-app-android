package com.notesmakers.common_ui.composables.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String,
    focusManager: FocusManager,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }
    ),
    isPassword: Boolean = false,
    errorColorText: Color = Color.Red,
    errorMessage: String?,
) {
    val valueState = remember {
        mutableStateOf(TextFieldValue(""))
    }
    Column {
        OutlinedTextField(
            value = valueState.value,
            onValueChange = { newText ->
                valueState.value = newText
                onValueChange(newText.text)
            },
            isError = errorMessage.isNullOrBlank().not(),
            singleLine = true,
            maxLines = 1,
            label = { Text(text = labelText) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            placeholder = { Text(text = placeholderText) },
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        errorMessage.takeIf { it.isNullOrBlank().not() }?.let {
            Text(text = it, color = errorColorText, fontSize = 12.sp)
        }
    }
}
