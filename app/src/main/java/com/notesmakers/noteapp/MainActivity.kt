package com.notesmakers.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.notesmakers.common_ui.composables.buttons.BaseIconButton
import com.notesmakers.database.data.models.Note
import com.notesmakers.database.domain.GetNotesUseCase
import com.notesmakers.database.domain.InsertNoteUseCase
import com.notesmakers.database.domain.NotesDatabaseActions
import com.notesmakers.noteapp.presentation.MainScreen
import com.notesmakers.noteapp.ui.theme.NoteAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.annotation.Factory

class MainActivity : ComponentActivity() {

    private val getNotesUseCase: GetNotesUseCase by inject()
    private val insertNoteUseCase: InsertNoteUseCase by inject()

    //    private val insertNoteUseCase: String by inject()
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var list = remember {
                mutableStateListOf<Note>()
            }
            NoteAppTheme {
                LaunchedEffect(Unit) {
                    getNotesUseCase().collect {
                        it.list.toList().forEach {
                            list.add(it)
                        }
                    }
                }
//                MainScreen()
                Column(modifier = Modifier.fillMaxSize()) {
                    BaseIconButton(onClick = {
                        GlobalScope.launch {
                            insertNoteUseCase(
                                Note(
                                    title = "Title",
                                    description = "Description"
                                )
                            )
                        }
                    }, imageVector = Icons.Default.Add)
                    BaseIconButton(onClick = {
                        GlobalScope.launch {
                            getNotesUseCase().collect {
                                it.list.toList().forEach {
                                    list.add(it)
                                }
                            }
                        }
                    }, imageVector = Icons.Default.Build)
                    list.forEach {
                        Text(text = "${it.description} tekst")
                    }
                }
            }
        }
    }
}


