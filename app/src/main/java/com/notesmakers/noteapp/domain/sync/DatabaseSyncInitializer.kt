package com.notesmakers.noteapp.domain.sync

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer

class DatabaseSyncInitializer : Initializer<NotesTasksDataSource> {
    override fun create(context: Context): NotesTasksDataSource {
        return NotesTasksDataSource(WorkManager.getInstance(context)).apply {
            fetchNotesPeriodically()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(WorkManagerInitializer::class.java)
    }
}