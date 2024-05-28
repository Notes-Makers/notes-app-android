package com.notesmakers.noteapp.domain.sync

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val REFRESH_RATE_MINUTES = 15L
private const val FETCH_LATEST_NOTES_TASK = "FetchLatestNotesTask"
private const val TAG_FETCH_LATEST_NOTES = "FetchLatestNotesTaskTag"

class NotesTasksDataSource(
    private val workManager: WorkManager
) {
    fun fetchNotesPeriodically() {
        val fetchNewsRequest = PeriodicWorkRequestBuilder<RefreshLatestNotesWorker>(
            REFRESH_RATE_MINUTES, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
            .addTag(TAG_FETCH_LATEST_NOTES)
        workManager.enqueueUniquePeriodicWork(
            FETCH_LATEST_NOTES_TASK,
            ExistingPeriodicWorkPolicy.KEEP,
            fetchNewsRequest.build()
        )
    }

    fun cancelFetchingNewsPeriodically() {
        workManager.cancelAllWorkByTag(TAG_FETCH_LATEST_NOTES)
    }
}