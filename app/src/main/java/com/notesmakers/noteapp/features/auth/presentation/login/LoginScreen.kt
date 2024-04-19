package com.notesmakers.noteapp.features.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notesmakers.noteapp.features.destinations.LoginScreenDestination
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.composables.buttons.BaseWideButton
import com.notesmakers.ui.composables.inputs.BaseTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
) {
    LoginScreen(navBack = {
        navigator.popBackStack()
    })
}

@Composable
private fun LoginScreen(navBack: () -> Unit) {
    val focusManager = LocalFocusManager.current

    var emailErrorMessage by remember { mutableStateOf("") }
    var passwordErrorMessage by remember { mutableStateOf("") }

    Box {
        BaseIconButton(
            onClick = navBack, imageVector = Icons.Default.Clear
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Login", modifier = Modifier.padding(vertical = 8.dp), fontSize = 22.sp)
            BaseTextField(
                modifier = Modifier.padding(bottom = 8.dp),
                onValueChange = { emailErrorMessage = it },
                labelText = "Enter email",
                placeholderText = "email",
                focusManager = focusManager,
                errorMessage = emailErrorMessage,
            )
            BaseTextField(
                modifier = Modifier.padding(bottom = 12.dp),
                onValueChange = { passwordErrorMessage = it },
                labelText = "Enter password",
                placeholderText = "password",
                isPassword = true,
                focusManager = focusManager,
                errorMessage = emailErrorMessage,
            )
            BaseWideButton(
                modifier = Modifier,
                label = "Log In",
                onClick = {},
            )
            FooterSignInScreen(onClick = {})
        }
    }
}

@Composable
private fun FooterSignInScreen(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(text = "I am new user", fontSize = 12.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Register",
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Black, Color.Black)
                )
            ),
            modifier = Modifier.clickable(onClick = onClick),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

fun DestinationsNavigator.goToLoginScreenDestination() = navigate(LoginScreenDestination)