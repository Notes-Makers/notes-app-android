package com.notesmakers.noteapp.presentation.auth.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notesmakers.noteapp.data.auth.EmailInputError
import com.notesmakers.noteapp.data.auth.NameInputError
import com.notesmakers.noteapp.data.auth.PasswordInputError
import com.notesmakers.noteapp.data.auth.SurnameInputError
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.presentation.base.SnackbarHandler
import com.notesmakers.noteapp.presentation.destinations.RegistrationScreenDestination
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.composables.buttons.BaseWideButton
import com.notesmakers.ui.composables.inputs.BaseTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun RegistrationScreen(
    navigator: DestinationsNavigator,
    snackbarHandler: SnackbarHandler,
    registrationViewModel: RegistrationViewModel = koinViewModel()
) {
    val registerState = registrationViewModel.registerState.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        registrationViewModel.messageEvent.collect {
            when (it) {
                is BaseViewModel.MessageEvent.Error -> snackbarHandler.showErrorSnackbar(message = it.error)
                BaseViewModel.MessageEvent.Success -> {
                    snackbarHandler.showSuccessSnackbar(message = "Zarejestrowałeś nowego użytkownika, zaloguj się by kontynuować")
                    navigator.popBackStack()
                }
            }

        }
    }
    RegistrationScreen(
        registerState = registerState as? RegistrationViewModel.RegisterState.Error,
        navBack = { navigator.popBackStack() },
        register = { name, surname, email, nickname, password ->
            registrationViewModel.register(
                name = name,
                surname = surname,
                email = email,
                nickname = nickname,
                password = password
            )
        }
    )
}

@Composable
private fun RegistrationScreen(
    registerState: RegistrationViewModel.RegisterState.Error?,
    navBack: () -> Unit,
    register: (
        name: String,
        surname: String,
        email: String,
        nickname: String,
        password: String
    ) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var nameText by remember { mutableStateOf("") }
    var surnameText by remember { mutableStateOf("") }
    var nicknameText by remember { mutableStateOf("") }


    Box {
        BaseIconButton(
            onClick = navBack, imageVector = Icons.Default.Clear,
            tint = MaterialTheme.colorScheme.onBackground,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Register",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = 22.sp
            )
            BaseTextField(
                modifier = Modifier.padding(bottom = 8.dp),
                onValueChange = { nameText = it },
                labelText = "Enter name",
                placeholderText = "name",
                focusManager = focusManager,
                errorMessage = registerState?.errors?.find { it is NameInputError }
                    ?.onError()
                    ?.let { error ->
                        stringResource(
                            id = error
                        )
                    }
            )
            BaseTextField(
                modifier = Modifier.padding(bottom = 8.dp),
                onValueChange = { surnameText = it },
                labelText = "Enter surname",
                placeholderText = "surname",
                focusManager = focusManager,
                errorMessage = registerState?.errors?.find { it is SurnameInputError }
                    ?.onError()
                    ?.let { error ->
                        stringResource(
                            id = error
                        )
                    }
            )
            BaseTextField(
                modifier = Modifier.padding(bottom = 8.dp),
                onValueChange = { nicknameText = it },
                labelText = "Enter nickname",
                placeholderText = "nickname",
                focusManager = focusManager,
                errorMessage = registerState?.errors?.find { it is SurnameInputError }
                    ?.onError()
                    ?.let { error ->
                        stringResource(
                            id = error
                        )
                    }
            )
            BaseTextField(
                modifier = Modifier.padding(bottom = 8.dp),
                onValueChange = { emailText = it },
                labelText = "Enter email",
                placeholderText = "email",
                focusManager = focusManager,
                errorMessage = registerState?.errors?.find { it is EmailInputError }
                    ?.onError()
                    ?.let { error ->
                        stringResource(
                            id = error
                        )
                    }
            )
            BaseTextField(
                modifier = Modifier.padding(bottom = 12.dp),
                onValueChange = { passwordText = it },
                labelText = "Enter password",
                placeholderText = "password",
                isPassword = true,
                focusManager = focusManager,
                errorMessage = registerState?.errors?.find { it is PasswordInputError }
                    ?.onError()
                    ?.let { error ->
                        stringResource(
                            id = error
                        )
                    }
            )
            BaseWideButton(
                modifier = Modifier.padding(top = 5.dp),
                label = "Register",
                onClick = {
                    register(
                        nameText,
                        surnameText,
                        emailText,
                        nicknameText,
                        passwordText
                    )
                },
            )
        }
    }
}

fun DestinationsNavigator.goToRegistrationScreenDestination() = navigate(
    RegistrationScreenDestination
)