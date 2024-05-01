package com.notesmakers.noteapp.domain

import android.content.Context
import android.util.Log
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
import com.notesmakers.database.data.toBitmapDrawableModel
import com.notesmakers.database.data.toPathDrawableModel
import com.notesmakers.database.data.toTextDrawableModel
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.type.ItemType
import com.notesmakers.noteapp.data.notes.api.BaseNote
import com.notesmakers.noteapp.data.notes.api.BaseNotesInfo
import com.notesmakers.noteapp.data.notes.api.BasePage
import com.notesmakers.noteapp.data.notes.api.BasePagesInfo
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.di.DatabaseDomainModule
import com.notesmakers.noteapp.di.NotesNetworkDomainModule
import com.notesmakers.noteapp.extension.formatLocalDateTimeToIsoString
import com.notesmakers.noteapp.extension.localDateFromTimeStamp
import com.notesmakers.noteapp.presentation.notes.creation.NoteMode
import com.notesmakers.noteapp.presentation.notes.creation.toNoteType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


interface NotesRepository {
    suspend fun syncNotes()
}

@Factory
class NotesRepositoryImpl(
    private val remoteDataSource: NotesRemoteDataSource,
    private val localDataSource: LocalDataSource
) : NotesRepository {
    override suspend fun syncNotes() {
        Log.e("TAG", "syncNotes:  ", )
        localDataSource.fetchDatabaseNotes().value.apply {

            Log.e("TAG", "syncNotes: ${this.size} ", )
//            Zapis z serwera do Bazy danych
            val apiNotes = remoteDataSource.fetchApiNotes()
            apiNotes.filter { remoteNote ->
                this.find {
                    it.remoteId == remoteNote.noteId
                } == null
            }.forEach { remoteUnsavedNote ->
                Log.e("TAG", "syncNotes: ${remoteUnsavedNote.noteId}", )
                val noteDetails = remoteDataSource.getNote(
                    remoteUnsavedNote.noteId!!
                )
                localDataSource.addNote(
                    remoteNoteId = remoteUnsavedNote.noteId,
                    name = noteDetails.name!!,
                    description = noteDetails.description!!,
                    noteType = NoteMode.PAINT_NOTE.toNoteType(),
                    createdAt = System.currentTimeMillis(), //TODO TIME
                    pages = noteDetails.pages.map { page ->
                        PageOutputModel(
                            id = page.id!!,
                            remotePageId = null,
                            createdAt = System.currentTimeMillis(), //TODO TIME
                            createdBy = page.createdBy!!,
                            modifiedAt = System.currentTimeMillis(), //TODO TIME
                            modifiedBy = page.modifiedBy!!,
                            bitmapDrawable = page.items?.filter { it?.type == ItemType.IMG }
                                ?.mapNotNull { bitmap ->
                                    BitmapDrawableModel(
                                        remoteItemId = bitmap?.content?.onImgOutputType?.noteId,
                                        width = bitmap?.position?.width?.toInt() ?: 0,
                                        height = bitmap?.position?.height?.toInt() ?: 0,
                                        scale = 1f,//TODO TIME
                                        offsetX = bitmap?.position?.posX?.toFloat() ?: 0f,
                                        offsetY = bitmap?.position?.posY?.toFloat() ?: 0f,
                                        bitmap = "",//TODO bitmap
                                        bitmapUrl = "",//TODO bitmap
                                        createdAt = System.currentTimeMillis(),//TODO bitmap

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
                                        createdAt = System.currentTimeMillis(),//TODO Time

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
                                        createdAt = System.currentTimeMillis(),
                                    )

                                } ?: emptyList(),
                        )
                    }.takeIf { isNotEmpty() } ?: listOf(
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
//--------------------------------------------------------------------------------------------------------
//                localDataSource.addNote(
//                    name = remoteUnsavedNote.name ?: "",
//                    description = remoteUnsavedNote.description ?: "",
//                    createdBy = remoteUnsavedNote.createdBy ?: formatLocalDateTimeToIsoString(
//                        LocalDateTime.now()
//                    ),
//                    noteType = remoteUnsavedNote.n
//                )
            }
//            Sprawdzenia aktualizacji notatek
//            //Krok 1 pobierz wszystkie notatki z serwera
//            //Krok 2 Przefiltruj te dane z bazy danych które nie mają RemoteID
//            //Krok 3 Prównuj modifyAt z Bazą Danych
//            val apiNotes = remoteDataSource.fetchApiNotes()
//            filter { it.remoteId != null }.forEach { databaseNote ->
//                apiNotes.find { it.noteId == databaseNote.remoteId }?.let { apiNote ->
//                    Log.e("TAG", "syncNotes: ", )
//                }
//            }
//            Wysłanie notatki na serwer
            //Krok 1 Wyślij wszystkie  na serwer jeżeli nie mają remote ID
            //Krok 2 Dla uzyskanego ID wpisz do bazy danych nowy zaktualizowany rekord ID
            //Krok 3 Do utworzonej notatki na serwerze dorzuć page i itemy
            //Krok 4 Po wysłaniu wszystkich Itemów i Page dla nie posiadającuch remoteID
//            filter { it.remoteId.isNullOrBlank() }
//                .forEach { databaseNote ->
//                    localDataSource.updateRemoteNoteId(
//                        noteId = databaseNote.id,
//                        remoteNoteId = remoteDataSource.addNote(
//                            name = databaseNote.name,
//                            description = databaseNote.description,
//                            createdBy = databaseNote.createdBy,
//                            isShared = databaseNote.isShared,
//                            createdAt = formatLocalDateTimeToIsoString(databaseNote.createdAt),
//                            isPrivate = databaseNote.isPrivate,
//                            modifiedAt = formatLocalDateTimeToIsoString(databaseNote.modifiedAt),
//                            modifiedBy = databaseNote.modifiedBy,
//                        )
//                    )?.pages?.forEach { databasePage ->
//                        remoteDataSource.addPage(
//                            noteId = databaseNote.id,
//                            pageId = databasePage.id, //TODO DO SPRAWDZENIA CZY NA PEWNO
//                            createdAt = formatLocalDateTimeToIsoString(databasePage.createdAt),
//                            createdBy = databaseNote.createdBy,
//                            modifiedAt = formatLocalDateTimeToIsoString(databasePage.modifiedAt),
//                            modifiedBy = databaseNote.modifiedBy,
//                        )
//
//                        databasePage.bitmapDrawables.forEach {
//                            //TODO   //Bitmap jak dodam multiparta
//
//                        }
//                        databasePage.pathDrawables.forEach { databasePathItem ->
//                            remoteDataSource.addItem(
//                                noteId = databaseNote.id,
//                                pageId = databasePage.id,
//                                itemId = databasePathItem.id,
//                                itemType = ItemType.SVG,//TODO PATH SPRAWDZENIA CZY NA PEWNO
//                                imgContent = null,
//                                pathContent = ApiPath(
//                                    strokeWidth = databasePathItem.strokeWidth.toDouble(),
//                                    color = databasePathItem.color,
//                                    alpha = databasePathItem.alpha.toDouble(),
//                                    eraseMode = databasePathItem.eraseMode,
//                                    path = databasePathItem.path,
//                                ),
//                                textContent = null,
//                                itemPosX = 0.0,//TODO
//                                itemPosY = 0.0,//TODO
//                                itemWidth = 0.0,//TODO
//                                itemHeight = 0.0,//TODO
//                                itemCreatedAt = formatLocalDateTimeToIsoString(databasePathItem.createdAt.localDateFromTimeStamp()),
//                                itemCreatedBy = "GUEST", //TODO
//                                itemModifiedAt = formatLocalDateTimeToIsoString(databasePathItem.createdAt.localDateFromTimeStamp()),
//                                itemModifiedBy = "GUEST", //TODO
//                                itemHash = "itemHash", //TODO
//                            )
//                        }
//                        databasePage.textDrawables.forEach { databaseTextItem ->
//                            remoteDataSource.addItem(
//                                noteId = databaseNote.id,
//                                pageId = databasePage.id,
//                                itemId = databaseTextItem.id,
//                                itemType = ItemType.SVG,//TODO PATH SPRAWDZENIA CZY NA PEWNO
//                                imgContent = null,
//                                pathContent = null,
//                                textContent = ApiText(
//                                    text = databaseTextItem.text,
//                                    color = databaseTextItem.color,
//                                ),
//                                itemPosX = 0.0,//TODO
//                                itemPosY = 0.0,//TODO
//                                itemWidth = 0.0,//TODO
//                                itemHeight = 0.0,//TODO
//                                itemCreatedAt = formatLocalDateTimeToIsoString(databaseTextItem.createdAt.localDateFromTimeStamp()),
//                                itemCreatedBy = "GUEST", //TODO
//                                itemModifiedAt = formatLocalDateTimeToIsoString(databaseTextItem.createdAt.localDateFromTimeStamp()),
//                                itemModifiedBy = "GUEST", //TODO
//                                itemHash = "itemHash", //TODO
//                            )
//                        }
//                    }
//                }
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
        name: String,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String
    ): String = withContext(ioDispatcher) {
        networkDomain.addNote(
            name = name,
            description = description,
            createdBy = createdBy,
            isShared = isShared,
            createdAt = createdAt,
            isPrivate = isPrivate,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy
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