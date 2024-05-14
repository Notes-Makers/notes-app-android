package com.notesmakers.noteapp.domain.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshLatestNotesWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val notesSyncRepository: NotesSyncRepository by inject()
    override suspend fun doWork(): Result = try {
        notesSyncRepository.syncNotes()
        Result.success()
    } catch (error: Throwable) {
        Result.failure()
    }
}