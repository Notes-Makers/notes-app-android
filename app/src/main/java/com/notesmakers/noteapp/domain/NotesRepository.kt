package com.notesmakers.noteapp.domain

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import androidx.work.WorkerParameters
import com.notesmakers.database.data.entities.UNDEFINED
import com.notesmakers.database.data.models.BitmapDrawableModel
import com.notesmakers.database.data.models.PageOutputModel
import com.notesmakers.database.data.models.PathDrawableModel
import com.notesmakers.database.data.models.QuickNoteModel
import com.notesmakers.database.data.models.TextDrawableModel
import com.notesmakers.network.data.api.ApiContent
import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiNoteType
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiPosition
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.type.ItemType
import com.notesmakers.noteapp.data.notes.api.BaseNote
import com.notesmakers.noteapp.data.notes.api.BaseNotesInfo
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.data.notes.local.toApiNoteType
import com.notesmakers.noteapp.data.notes.local.toNoteDrawableType
import com.notesmakers.noteapp.di.DatabaseDomainModule
import com.notesmakers.noteapp.di.NotesNetworkDomainModule
import com.notesmakers.noteapp.extension.formatZonedDateTimeToIsoString
import com.notesmakers.noteapp.extension.parseStringToZonedDateTime
import com.notesmakers.noteapp.extension.zoneDateFromTimeStamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
import java.util.concurrent.TimeUnit


interface NotesRepository {
    suspend fun syncNotes()
    suspend fun addNote()
}

@Factory
class NotesRepositoryImpl(
    private val remoteDataSource: NotesRemoteDataSource,
    private val localDataSource: LocalDataSource
) : NotesRepository {
    override suspend fun syncNotes() {
        localDataSource.fetchDatabaseNotes().value.apply {
            val apiNotes = remoteDataSource.fetchApiNotes()
            syncUnFetchedNotes(apiNotes)
            syncUnsentNotes()
        }
    }

    override suspend fun addNote() {
        remoteDataSource.addNote(
            name = "nie dzialajaca Notatka",
            apiNoteType = ApiNoteType.PAPER,
            description = "To opis nowej notaki",
            createdBy = "krystian.korbecki@gmail.com",
            isShared = false,
            createdAt = "2021-09-30T15:30:00+01:00",
            isPrivate = true,
            modifiedAt = "2021-09-30T15:30:00+01:00",
            modifiedBy = "krystian.korbecki@gmail.com",
            pages = listOf(
                ApiGetPage(
                    id = "idexample23",
                    isDeleted = false,
                    createdAt = "2021-09-30T15:30:00+01:00",
                    createdBy = "krystian.korbecki@gmail.com",
                    modifiedAt = "2021-09-30T15:30:00+01:00",
                    modifiedBy = "krystian.korbecki@gmail.com",
                    items = listOf(
                        ApiGetItem(
                            id = "firstitem",
                            type = ItemType.MD,
                            isDeleted = false,
                            content = ApiContent(
                                typename = "typename",
                                onTextOutputType = ApiText(
                                    text = "Dziala",
                                    color = "#ff000000"
                                ),
                                onImgOutputType = null,
                                onPathOutputType = null,
                            ),
                            createdAt = "2021-09-30T15:30:00+01:00",
                            createdBy = "krystian.korbecki@gmail.com",
                            modifiedAt = "2021-09-30T15:30:00+01:00",
                            modifiedBy = "krystian.korbecki@gmail.com",
                            hash = null,
                            position = ApiPosition(
                                posX = 10.0,
                                posY = 10.0,
                                width = 120.0,
                                height = 120.0
                            )
                        )
                    ),
                ),

                ApiGetPage(
                    id = "idexample234",
                    isDeleted = false,
                    createdAt = "2021-09-30T15:30:00+01:00",
                    createdBy = "krystian.korbecki@gmail.com",
                    modifiedAt = "2021-09-30T15:30:00+01:00",
                    modifiedBy = "krystian.korbecki@gmail.com",
                    items = listOf(
                        ApiGetItem(
                            id = "firstitem222",
                            type = ItemType.SVG,
                            isDeleted = false,
                            content = ApiContent(
                                typename = "typename",
                                onTextOutputType = null,
                                onImgOutputType = null,
                                onPathOutputType = ApiPath(
                                    strokeWidth = 15.0,
                                    color = "#ff000000",
                                    alpha = 1.0,
                                    eraseMode = false,
                                    path = "M 4 8 L 10 1 L 13 0 L 12 3 L 5 9 C 6 10 6 11 7 10 C 7 11 8 12 7 12 A 1.42 1.42 0 0 1 6 13 A 5 5 0 0 0 4 10 Q 3.5 9.9 3.5 10.5 T 2 11.8 T 1.2 11 T 2.5 9.5 T 3 9 A 5 5 90 0 0 0 7 A 1.42 1.42 0 0 1 1 6 C 1 5 2 6 3 6 C 2 7 3 7 4 8 M 10 1 L 10 3 L 12 3 L 10.2 2.8 L 10 1"
                                ),
                            ),
                            createdAt = "2021-09-30T15:30:00+01:00",
                            createdBy = "krystian.korbecki@gmail.com",
                            modifiedAt = "2021-09-30T15:30:00+01:00",
                            modifiedBy = "krystian.korbecki@gmail.com",
                            hash = null,
                            position = ApiPosition(
                                posX = 10.0,
                                posY = 10.0,
                                width = 120.0,
                                height = 120.0
                            )
                        )
                    ),
                )
            )
        )
    }

    /**
     * Function get all notes from locally and send what not have remote Id to server.
     * Current type support : Text, Path - Bitmap is not ready
     * Known issues - Date is Wrong
     */
    private suspend fun List<Note>.syncUnsentNotes() {
        this.filter { localNote ->
            localNote.remoteId.isNullOrBlank()
        }.forEach { unsentNotes ->
            val noteId = UUID.randomUUID().toString()
            localDataSource.updateRemoteNoteId(noteId, remoteDataSource.addNote(
                id = noteId,
                name = unsentNotes.name,
                apiNoteType = unsentNotes.noteType.toNoteDrawableType().toApiNoteType(),
                description = unsentNotes.description,
                createdBy = unsentNotes.createdBy,
                isShared = unsentNotes.isShared,
                createdAt = formatZonedDateTimeToIsoString(unsentNotes.createdAt),
                isPrivate = unsentNotes.isPrivate,
                modifiedAt = formatZonedDateTimeToIsoString(unsentNotes.modifiedAt),
                modifiedBy = unsentNotes.modifiedBy,
                pages = unsentNotes.pages.map { pageOutput ->
                    ApiGetPage(
                        id = pageOutput.id,
                        isDeleted = false,
                        createdAt = formatZonedDateTimeToIsoString(pageOutput.createdAt),
                        createdBy = pageOutput.createdBy,
                        modifiedAt = formatZonedDateTimeToIsoString(pageOutput.modifiedAt),
                        modifiedBy = pageOutput.modifiedBy,
                        items = pageOutput.bitmapDrawables.map { item ->
                            ApiGetItem(
                                id = item.id,
                                type = ItemType.IMG,
                                isDeleted = false,
                                content = ApiContent(
                                    typename = "img",
                                    onTextOutputType = null,
                                    onImgOutputType = ApiImg(
                                        noteId = noteId,
                                        scale = item.scale,
                                        itemId = item.id,
                                    ),
                                    onPathOutputType = null,
                                ),
                                createdAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                createdBy = pageOutput.createdBy,//TODO create Owner
                                modifiedAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                modifiedBy = pageOutput.createdBy,//TODO create Owner
                                position = ApiPosition(
                                    posX = item.offsetX.toDouble(),
                                    posY = item.offsetX.toDouble(),
                                    width = item.width.toDouble(),
                                    height = item.height.toDouble(),
                                ), hash = null
                            )
                        } + pageOutput.pathDrawables.map { item ->
                            ApiGetItem(
                                id = item.id,
                                type = ItemType.SVG,
                                isDeleted = false,
                                content = ApiContent(
                                    typename = "svg",
                                    onTextOutputType = null,
                                    onImgOutputType = null,
                                    onPathOutputType = ApiPath(
                                        strokeWidth = item.strokeWidth.toDouble(),
                                        color = item.color,
                                        alpha = item.alpha.toDouble(),
                                        eraseMode = item.eraseMode,
                                        path = item.path,
                                    ),
                                ),
                                createdAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                createdBy = pageOutput.createdBy,//TODO create Owner
                                modifiedAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                modifiedBy = pageOutput.createdBy,//TODO create Owner
                                position = null, hash = null
                            )
                        } + pageOutput.textDrawables.map { item ->
                            ApiGetItem(
                                id = item.id,
                                type = ItemType.SVG,
                                isDeleted = false,
                                content = ApiContent(
                                    typename = "svg",
                                    onTextOutputType = ApiText(
                                        text = item.text,
                                        color = item.color
                                    ),
                                    onImgOutputType = null,
                                    onPathOutputType = null,
                                ),
                                createdAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                createdBy = pageOutput.createdBy, //TODO create Owner
                                modifiedAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                modifiedBy = pageOutput.createdBy,//TODO create Owner
                                position = ApiPosition(
                                    posX = item.offsetX.toDouble(),
                                    posY = item.offsetY.toDouble(),
                                    width = null,
                                    height = null,
                                ), hash = null
                            )
                        },
                    )
                }
            ))
        }

    }

    /**
     * Function get all notes from api and save locally result.
     * Current type support : Text, Path - Bitmap is not ready
     * Known issues - Date is Wrong
     */
    private suspend fun List<Note>.syncUnFetchedNotes(apiNotes: List<BaseNotesInfo>) {
        apiNotes.filter { remoteNote ->
            this.find {
                it.remoteId == remoteNote.noteId //todo do sprawdzenia chyba źle
            } == null
        }.forEach { remoteUnsavedNote ->
            val noteDetails = remoteDataSource.getNote(
                remoteUnsavedNote.noteId!!
            )
            localDataSource.addNote(
                remoteNoteId = remoteUnsavedNote.noteId,
                name = remoteUnsavedNote.name!!,
                description = remoteUnsavedNote.description!!,
                noteType = remoteUnsavedNote.type.type,
                createdBy = remoteUnsavedNote.createdBy!!,
                createdAt = parseStringToZonedDateTime(remoteUnsavedNote.createdAt as String).toEpochSecond(),
                pages = noteDetails.pages.map { page ->
                    PageOutputModel(
                        id = page.id!!,
                        remotePageId = null,
                        createdAt = parseStringToZonedDateTime(page.createdAt as String).toEpochSecond(),
                        createdBy = page.createdBy!!,
                        modifiedAt = parseStringToZonedDateTime(page.modifiedAt as String).toEpochSecond(),
                        modifiedBy = page.modifiedBy!!,
                        bitmapDrawable = page.items?.filter { it?.type == ItemType.IMG }
                            ?.mapNotNull { bitmap ->
                                BitmapDrawableModel(
                                    remoteItemId = bitmap?.content?.onImgOutputType?.noteId,
                                    width = bitmap?.position?.width?.toInt() ?: 0,
                                    height = bitmap?.position?.height?.toInt() ?: 0,
                                    scale = bitmap?.content?.onImgOutputType?.scale ?: 1f,
                                    offsetX = bitmap?.position?.posX?.toFloat() ?: 0f,
                                    offsetY = bitmap?.position?.posY?.toFloat() ?: 0f,
                                    bitmap = "",//TODO bitmap ważne to dodać totalnie inaczej niż teraz to działa
                                    bitmapUrl = "",//TODO bitmap
                                    createdAt = parseStringToZonedDateTime(bitmap?.createdAt as String).toEpochSecond(),
                                )
                            } ?: emptyList(),
                        pathDrawables = page.items?.filter { it?.type == ItemType.SVG }
                            ?.mapNotNull { path ->
                                PathDrawableModel(
                                    remoteItemId = path?.id,
                                    strokeWidth = path?.content?.onPathOutputType?.strokeWidth?.toFloat()
                                        ?: 0f,
                                    color = path?.content?.onPathOutputType?.color ?: "#FFF",
                                    alpha = path?.content?.onPathOutputType?.alpha?.toFloat()
                                        ?: 0f,
                                    eraseMode = path?.content?.onPathOutputType?.eraseMode
                                        ?: false,
                                    path = path?.content?.onPathOutputType?.path
                                        ?: "",
                                    createdAt = parseStringToZonedDateTime(path?.createdAt as String).toEpochSecond(),
                                )
                            } ?: emptyList(),
                        textDrawables = page.items?.filter { it?.type == ItemType.MD }
                            ?.mapNotNull { text ->
                                TextDrawableModel(
                                    remoteItemId = text?.id,
                                    text = text?.content?.onTextOutputType?.text ?: "",
                                    color = text?.content?.onTextOutputType?.color ?: "",
                                    offsetX = text?.position?.posX?.toFloat() ?: 0f,
                                    offsetY = text?.position?.posY?.toFloat() ?: 0f,
                                    createdAt = parseStringToZonedDateTime(text?.createdAt as String).toEpochSecond(),
                                )

                            } ?: emptyList(),
                    )
                }.takeIf { isNotEmpty() } ?: listOf(
                    //Zabezpiecznie przed tym by nie było pustej listy
                    PageOutputModel(
                        remotePageId = null,
                        createdAt = System.currentTimeMillis(),
                        createdBy = "GUEST",
                        modifiedAt = System.currentTimeMillis(),
                        modifiedBy = "GUEST"
                    )
                ),
                quickNote = QuickNoteModel(id = "", text = "")
            )
        }
    }
}

@Factory
class LocalDataSource(
    private val databaseDomainModule: DatabaseDomainModule,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchDatabaseNotes() =
        databaseDomainModule.getNotes().stateIn(
            CoroutineScope(ioDispatcher)
        )

    suspend fun updateRemoteNoteId(
        noteId: String,
        remoteNoteId: String
    ): Note? = databaseDomainModule.updateRemoteNoteId(noteId, remoteNoteId)

    suspend fun addNote(
        remoteNoteId: String,
        name: String,
        description: String,
        createdAt: Long,
        createdBy: String = "",
        pages: List<PageOutputModel>,
        modifiedBy: String = createdBy,
        modifiedAt: Long = System.currentTimeMillis(),
        isPrivate: Boolean = false,
        isShared: Boolean = false,
        isPinned: Boolean = false,
        tag: List<String> = listOf(),
        quickNote: QuickNoteModel,
        noteType: String = UNDEFINED,
    ): Note = databaseDomainModule.createCompleteNote(
        name = name,
        description = description,
        noteType = noteType,
        createdAt = createdAt,
        createdBy = createdBy,
        pages = pages,
        modifiedBy = modifiedBy,
        modifiedAt = modifiedAt,
        isPrivate = isPrivate,
        isShared = isShared,
        isPinned = isPinned,
        tag = tag,
        quickNote = quickNote,
        remoteNoteId = remoteNoteId
    )

}

@Factory
class NotesRemoteDataSource(
    private val networkDomain: NotesNetworkDomainModule,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchApiNotes(): List<BaseNotesInfo> =
        withContext(ioDispatcher) {
            networkDomain.getNotesInfo()
        }

    suspend fun getNote(noteId: String): BaseNote = withContext(ioDispatcher) {
        networkDomain.getNote(noteId)
    }

    suspend fun addNote(
        id: String = UUID.randomUUID().toString(),
        name: String,
        apiNoteType: ApiNoteType,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String,
        pages: List<ApiGetPage>
    ): String = withContext(ioDispatcher) {
        networkDomain.addNote(
            id = id,
            name = name,
            apiNoteType = apiNoteType,
            description = description,
            createdBy = createdBy,
            isShared = isShared,
            createdAt = createdAt,
            isPrivate = isPrivate,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy,
            pages = pages
        )
    }

    suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String
    ) = withContext(ioDispatcher) {
        networkDomain.addPage(
            noteId = noteId,
            pageId = pageId,
            createdAt = createdAt,
            createdBy = createdBy,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy
        )
    }

    suspend fun addItem(
        noteId: String,
        pageId: String,
        itemId: String,
        itemType: ItemType,
        imgContent: ApiImg?,
        pathContent: ApiPath?,
        textContent: ApiText?,
        itemPosX: Double,
        itemPosY: Double,
        itemWidth: Double,
        itemHeight: Double,
        itemCreatedAt: String,
        itemCreatedBy: String,
        itemModifiedAt: String,
        itemModifiedBy: String,
        itemHash: String
    ) = withContext(ioDispatcher) {
        networkDomain.addItem(
            noteId = noteId,
            pageId = pageId,
            itemId = itemId,
            itemType = itemType,
            imgContent = imgContent,
            pathContent = pathContent,
            textContent = textContent,
            itemPosX = itemPosX,
            itemPosY = itemPosY,
            itemWidth = itemWidth,
            itemHeight = itemHeight,
            itemCreatedAt = itemCreatedAt,
            itemCreatedBy = itemCreatedBy,
            itemModifiedAt = itemModifiedAt,
            itemModifiedBy = itemModifiedBy,
            itemHash = itemHash
        )
    }
}

class RefreshLatestNotesWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val notesRepository: NotesRepository by inject()
    override suspend fun doWork(): Result = try {
        notesRepository.syncNotes()
        Result.success()
    } catch (error: Throwable) {
        Result.failure()
    }
}

private const val REFRESH_RATE_HOURS = 1L
private const val FETCH_LATEST_NOTES_TASK = "FetchLatestNotesTask"
private const val TAG_FETCH_LATEST_NOTES = "FetchLatestNotesTaskTag"

class NotesTasksDataSource(
    private val workManager: WorkManager
) {
    fun fetchNewsPeriodically() {
        val fetchNewsRequest = PeriodicWorkRequestBuilder<RefreshLatestNotesWorker>(
            REFRESH_RATE_HOURS, TimeUnit.HOURS
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

class DatabaseSyncInitializer : Initializer<NotesTasksDataSource> {
    override fun create(context: Context): NotesTasksDataSource {
        return NotesTasksDataSource(WorkManager.getInstance(context)).apply {
//            fetchNewsPeriodically()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(WorkManagerInitializer::class.java)
    }
}