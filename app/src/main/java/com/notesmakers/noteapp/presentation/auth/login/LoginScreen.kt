package com.notesmakers.noteapp.presentation.auth.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notesmakers.noteapp.data.auth.EmailInputError
import com.notesmakers.noteapp.data.auth.PasswordInputError
import com.notesmakers.noteapp.presentation.auth.registration.goToRegistrationScreenDestination
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.presentation.base.SnackbarHandler
import com.notesmakers.noteapp.presentation.destinations.LoginScreenDestination
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.composables.buttons.BaseWideButton
import com.notesmakers.ui.composables.inputs.BaseTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    snackbarHandler: SnackbarHandler,
    navigator: DestinationsNavigator,
) {
    val loginState = loginViewModel.loginState.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        loginViewModel.messageEvent.collect {
            when (it) {
                is BaseViewModel.MessageEvent.Error -> snackbarHandler.showErrorSnackbar(message = it.error)
                BaseViewModel.MessageEvent.Success -> {
                    resultNavigator.navigateBack(true)
                    snackbarHandler.showSuccessSnackbar(message = "Witamy zalogowanego użytkownika")
                }
            }

        }
    }
    LoginScreen(loginState = loginState as? LoginViewModel.LoginState.Error,
        navBack = {
            navigator.popBackStack()
        },
        navToRegistrationScreen = { navigator.goToRegistrationScreenDestination() },
        login = { email, password ->
            loginViewModel.login(
                email = email, password = password
            )
        })
}

@Composable
private fun LoginScreen(
    loginState: LoginViewModel.LoginState.Error?,
    navBack: () -> Unit,
    navToRegistrationScreen: () -> Unit,
    login: (email: String, password: String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
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
            BaseTextField(modifier = Modifier.padding(bottom = 8.dp),
                onValueChange = { emailText = it },
                labelText = "Enter email",
                placeholderText = "email",
                focusManager = focusManager,
                errorMessage = loginState?.errors?.find { it is EmailInputError }?.onError()
                    ?.let { error ->
                        stringResource(
                            id = error
                        )
                    })
            BaseTextField(modifier = Modifier.padding(bottom = 12.dp),
                onValueChange = { passwordText = it },
                labelText = "Enter password",
                placeholderText = "password",
                isPassword = true,
                focusManager = focusManager,
                errorMessage = loginState?.errors?.find { it is PasswordInputError }?.onError()
                    ?.let { error ->
                        stringResource(
                            id = error
                        )
                    })
            BaseWideButton(
                modifier = Modifier.padding(top = 5.dp),
                label = "Log In",
                onClick = {
                    keyboardController?.hide()
                    login(emailText, passwordText)
                },
            )
            FooterSignInScreen(onClick = { navToRegistrationScreen() })
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