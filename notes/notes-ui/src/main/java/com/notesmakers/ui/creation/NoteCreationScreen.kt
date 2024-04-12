package com.notesmakers.ui.creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.R
import com.notesmakers.ui.composables.buttons.BaseButton
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.composables.inputs.BaseTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.get

@Composable
@Destination
fun NoteCreationScreen(navigator: DestinationsNavigator, noteMode: NoteMode = NoteMode.QUICK_NOTE) {
    NoteCreationScreen(onBackNav = { navigator.popBackStack() }, noteMode = noteMode)
}

@Composable
private fun NoteCreationScreen(onBackNav: () -> Unit, noteMode: NoteMode) {
    Scaffold(
        topBar = {
            TopBarCreationScreen(onBackNav = onBackNav)
        },
    ) { innerPadding ->
        CreationPage(modifier = Modifier.padding(innerPadding), noteMode = noteMode)
    }
}

@Composable
private fun CreationPage(modifier: Modifier, noteMode: NoteMode) {
    val focusManager = LocalFocusManager.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = noteMode.icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
            )
            Column {
                Text(text = noteMode.title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(bottom = 4.dp))
                Text(
                    text = noteMode.desc,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.padding(bottom = 4.dp))
                HorizontalDivider()
            }

        }
        BaseTextField(
            modifier = Modifier.padding(bottom = 12.dp),
            onValueChange = { title = it },
            labelText = "Enter title",
            placeholderText = "title",
            isPassword = true,
            focusManager = focusManager,
            errorMessage = null,
        )
        BaseTextField(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .defaultMinSize(minHeight = 300.dp)
                .fillMaxWidth(),
            onValueChange = { description = it },
            labelText = "Enter description",
            placeholderText = "description",
            focusManager = focusManager,
            errorMessage = null,
            maxLines = 5,
            singleLine = false
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            BaseButton(
                modifier = Modifier,
                label = "Create",
                onClick = {},
            )
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBarCreationScreen(onBackNav: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BaseIconButton(
                    onClick = onBackNav,
                    imageVector = Icons.Default.Clear
                )
            }
        }
    )
}