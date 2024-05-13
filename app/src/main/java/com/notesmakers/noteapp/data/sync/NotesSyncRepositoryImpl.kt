package com.notesmakers.noteapp.data.sync

import com.notesmakers.database.data.models.BitmapDrawableModel
import com.notesmakers.database.data.models.PageOutputModel
import com.notesmakers.database.data.models.PathDrawableModel
import com.notesmakers.database.data.models.QuickNoteModel
import com.notesmakers.database.data.models.TextDrawableModel
import com.notesmakers.network.data.api.ApiContent
import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiPosition
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.type.ItemType
import com.notesmakers.noteapp.data.notes.api.BaseNotesInfo
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.data.notes.local.toApiNoteType
import com.notesmakers.noteapp.data.notes.local.toNoteDrawableType
import com.notesmakers.noteapp.domain.auth.GetOwnerUseCase
import com.notesmakers.noteapp.domain.sync.NotesSyncRepository
import com.notesmakers.noteapp.extension.formatZonedDateTimeToIsoString
import com.notesmakers.noteapp.extension.parseStringToZonedDateTime
import com.notesmakers.noteapp.extension.toTimestampDataType
import com.notesmakers.noteapp.extension.zoneDateFromTimeStamp
import org.koin.core.annotation.Factory
import java.util.UUID


@Factory
class NotesSyncRepositoryImpl(
    private val remoteDataSource: NotesRemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val getOwnerUseCase: GetOwnerUseCase
) : NotesSyncRepository {
    override suspend fun syncNotes() {
        localDataSource.fetchDatabaseNotes().value.apply {
            val apiNotes = remoteDataSource.fetchApiNotes()
            syncUnFetchedNotes(apiNotes)
            syncUnsentNotes()
            updateNotes(apiNotes)
        }
    }

    /**
     * Update All notes by timestamp
     */
    private suspend fun List<Note>.updateNotes(apiNotes: List<BaseNotesInfo>) {

        this.forEach { noteLocal ->
            apiNotes.find { noteLocal.remoteId == it.noteId }?.let { noteRemote ->
                //Get Current Note
                val remoteDetailsNote = remoteDataSource.getNote(
                    noteRemote.noteId!!
                )
                if (noteLocal.modifiedAt.toLocalDateTime() > parseStringToZonedDateTime(noteRemote.modifiedAt as String).toLocalDateTime()) {
                    //Update Remote <-
                    //Add pages who not exist
                    noteLocal.pages.filter { localItem ->
                        remoteDetailsNote.pages.find {
                            it.id == localItem.id
                        } == null
                    }.forEach { localPage ->
                        remoteDataSource.addPage(
                            noteId = remoteDetailsNote.id ?: noteRemote.noteId,
                            pageId = localPage.id,
                            createdAt = formatZonedDateTimeToIsoString(localPage.createdAt),
                            createdBy = localPage.createdBy,
                            modifiedAt = formatZonedDateTimeToIsoString(localPage.modifiedAt),
                            modifiedBy = localPage.modifiedBy,
                        )
                    }
                    //Add items to Page on Api who not exist
                    noteLocal.pages.forEach { localPage ->
                        remoteDetailsNote.pages.find { localPage.id == it.id }
                            ?.let { remotePage ->
                                localPage.bitmapDrawables.filter { localItem ->
                                    remotePage.items?.find {
                                        it?.id == localItem.id && it.type == ItemType.IMG
                                    } == null
                                }.forEach { bitmap ->
                                    /*
                                    remoteDataSource.setBitmapUseCase(
                                        noteId = noteLocal.remoteId ?: noteLocal.id,
                                        itemId = bitmap.id,
                                        base64String = bitmap.bitmap
                                    )
                                    remoteDataSource.addItem(
                                        noteId = noteLocal.remoteId ?: noteRemote.noteId,
                                        pageId = remotePage.id ?: localPage.id,
                                        itemId = bitmap.id,
                                        itemType = ItemType.IMG,
                                        imgContent = ApiImg(
                                            noteId = noteLocal.remoteId ?: noteLocal.id,
                                            scale = bitmap.scale,
                                            itemId = bitmap.id,
                                        ),
                                        pathContent = null,
                                        textContent = null,
                                        itemPosX = bitmap.offsetX.toDouble(),
                                        itemPosY = bitmap.offsetY.toDouble(),
                                        itemWidth = 0.0,
                                        itemHeight = 0.0,
                                        itemCreatedAt = formatZonedDateTimeToIsoString(bitmap.createdAt.zoneDateFromTimeStamp()),
                                        itemCreatedBy = getOwnerUseCase(),
                                        itemModifiedAt = formatZonedDateTimeToIsoString(bitmap.createdAt.zoneDateFromTimeStamp()),
                                        itemModifiedBy = getOwnerUseCase(),
                                        itemHash = "itemHash".hashCode().toString()
                                    )*/
                                }
                                localPage.textDrawables.filter { localItem ->
                                    remotePage.items?.find {
                                        it?.id == localItem.id && it.type == ItemType.MD
                                    } == null
                                }.forEach { text ->
                                    remoteDataSource.addItem(
                                        noteId = noteLocal.remoteId ?: noteRemote.noteId,
                                        pageId = remotePage.id ?: localPage.id,
                                        itemId = text.id,
                                        itemType = ItemType.MD,
                                        imgContent = null,
                                        pathContent = null,
                                        textContent = ApiText(
                                            text = text.text,
                                            color = text.color
                                        ),
                                        itemPosX = text.offsetX.toDouble(),
                                        itemPosY = text.offsetY.toDouble(),
                                        itemWidth = 0.0,
                                        itemHeight = 0.0,
                                        itemCreatedAt = formatZonedDateTimeToIsoString(text.createdAt.zoneDateFromTimeStamp()),
                                        itemCreatedBy = getOwnerUseCase(),
                                        itemModifiedAt = formatZonedDateTimeToIsoString(text.createdAt.zoneDateFromTimeStamp()),
                                        itemModifiedBy = getOwnerUseCase(),
                                        itemHash = "itemHash".hashCode().toString()
                                    )
                                }
                                localPage.pathDrawables.filter { localItem ->
                                    remotePage.items?.find {
                                        it?.id == localItem.id && it.type == ItemType.SVG
                                    } == null
                                }.forEach { path ->
                                    remoteDataSource.addItem(
                                        noteId = noteLocal.remoteId ?: noteRemote.noteId,
                                        pageId = remotePage.id ?: localPage.id,
                                        itemId = path.id,
                                        itemType = ItemType.SVG,
                                        imgContent = null,
                                        pathContent = ApiPath(
                                            strokeWidth = path.strokeWidth.toDouble(),
                                            color = path.color,
                                            alpha = path.alpha.toDouble(),
                                            eraseMode = path.eraseMode,
                                            path = path.path,
                                        ),
                                        textContent = null,
                                        itemPosX = 0.0,
                                        itemPosY = 0.0,
                                        itemWidth = 0.0,
                                        itemHeight = 0.0,
                                        itemCreatedAt = formatZonedDateTimeToIsoString(path.createdAt.zoneDateFromTimeStamp()),
                                        itemCreatedBy = getOwnerUseCase(),
                                        itemModifiedAt = formatZonedDateTimeToIsoString(path.createdAt.zoneDateFromTimeStamp()),
                                        itemModifiedBy = getOwnerUseCase(),
                                        itemHash = "itemHash".hashCode().toString()
                                    )
                                }
                            }

                    }
                } else if (noteLocal.modifiedAt.toLocalDateTime() < parseStringToZonedDateTime(
                        noteRemote.modifiedAt
                    ).toLocalDateTime()
                ) {
                    //Update Local Database <-
                    //Add pages who not exist
                    remoteDetailsNote.pages.filter { remoteItem ->
                        noteLocal.pages.find {
                            it.id == remoteItem.id
                        } == null
                    }.forEach { remotePage ->
                        localDataSource.addPage(
                            noteId = noteLocal.id,
                            pageId = remotePage.id!!,
                            createdBy = remotePage.createdBy!!,
                            createdAt = parseStringToZonedDateTime(remotePage.createdAt as String).toInstant()
                                .toEpochMilli(),
                            modifiedBy = remotePage.modifiedBy!!,
                            modifiedAt = parseStringToZonedDateTime(remotePage.modifiedAt as String).toInstant()
                                .toEpochMilli(),
                        )
                    }
                    //Add items to Page who not exist
                    remoteDetailsNote.pages.forEach { remotePage ->
                        //find local Page to update
                        noteLocal.pages.find { remotePage.id == it.id }?.let { localPage ->
                            //Update items Local
                            remotePage.items?.filter { remoteItem ->
                                localPage.bitmapDrawables.find {
                                    it.id == remoteItem?.id && remoteItem.type == ItemType.IMG
                                } == null
                            }?.forEach { bitmap ->
                                //TODO bitmap
//                                localDataSource.addBitmapItem(
//                                    noteId = noteLocal.id,
//                                    id = bitmap?.id ?: UUID.randomUUID().toString(),
//                                    createdAt = parseStringToZonedDateTime(bitmap?.modifiedAt as String).toInstant()
//                                        .toEpochMilli(),
//                                    pageId = remotePage.id!!,
//                                    width = bitmap.position.width?.toInt() ?: 0,
//                                    height = bitmap.position.height?.toInt() ?: 0,
//                                    scale = bitmap.content?.onImgOutputType?.scale ?: 1f,
//                                    offsetX = bitmap.position.posX?.toFloat() ?: 0.0f,
//                                    offsetY = bitmap.position.posY?.toFloat() ?: 0.0f,
//                                    bitmap = remoteDataSource.getBitmapUseCase(
//                                        noteLocal.id,
//                                        bitmap.id!!
//                                    ),
//                                    bitmapUrl = ""
//                                )
                            }
                            remotePage.items?.filter { remoteItem ->
                                localPage.pathDrawables.find {
                                    it.id == remoteItem?.id && remoteItem.type == ItemType.SVG
                                } == null
                            }?.forEach { path ->
                                localDataSource.addPathItem(
                                    id = path?.id ?: UUID.randomUUID().toString(),
                                    createdAt = parseStringToZonedDateTime(path?.modifiedAt as String).toInstant()
                                        .toEpochMilli(),
                                    pageId = remotePage.id!!,
                                    strokeWidth = path.content?.onPathOutputType?.strokeWidth?.toFloat()
                                        ?: 0f,
                                    color = path.content?.onPathOutputType?.color
                                        ?: "#ff3b82f6",
                                    alpha = path.content?.onPathOutputType?.alpha?.toFloat()
                                        ?: 0f,
                                    eraseMode = path.content?.onPathOutputType?.eraseMode
                                        ?: false,
                                    path = path.content?.onPathOutputType?.path ?: "",
                                    noteId = noteLocal.id
                                )
                            }
                            remotePage.items?.filter { remoteItem ->
                                localPage.textDrawables.find {
                                    it.id == remoteItem?.id && remoteItem.type == ItemType.MD
                                } == null
                            }?.forEach { text ->
                                localDataSource.addTextItem(
                                    id = text?.id ?: UUID.randomUUID().toString(),
                                    createdAt = parseStringToZonedDateTime(text?.modifiedAt as String).toInstant()
                                        .toEpochMilli(),
                                    pageId = remotePage.id!!,
                                    text = text.content?.onTextOutputType?.text ?: "",
                                    color = text.content?.onTextOutputType?.color
                                        ?: "#ff3b82f6",
                                    offsetX = text.position.posX?.toFloat() ?: 0f,
                                    offsetY = text.position.posY?.toFloat() ?: 0f,
                                    noteId = noteLocal.id
                                )
                            }
                        }
                    }
                }
            }
        }

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
            localDataSource.updateRemoteNoteId(unsentNotes.id, remoteDataSource.addNote(
                name = unsentNotes.name,
                apiNoteType = unsentNotes.noteType.toNoteDrawableType().toApiNoteType(),
                description = unsentNotes.description,
                createdBy = unsentNotes.createdBy.takeIf { !isNullOrEmpty() }
                    ?: getOwnerUseCase(),
                isShared = unsentNotes.isShared,
                createdAt = formatZonedDateTimeToIsoString(unsentNotes.createdAt),
                isPrivate = unsentNotes.isPrivate,
                modifiedAt = formatZonedDateTimeToIsoString(unsentNotes.modifiedAt),
                modifiedBy = unsentNotes.modifiedBy.takeIf { !isNullOrEmpty() }
                    ?: getOwnerUseCase(),
                pages = unsentNotes.pages.map { pageOutput ->
                    ApiGetPage(
                        id = pageOutput.id,
                        isDeleted = false,
                        createdAt = formatZonedDateTimeToIsoString(pageOutput.createdAt),
                        createdBy = pageOutput.createdBy.takeIf { !isNullOrEmpty() }
                            ?: getOwnerUseCase(),
                        modifiedAt = formatZonedDateTimeToIsoString(pageOutput.modifiedAt),
                        modifiedBy = pageOutput.modifiedBy.takeIf { !isNullOrEmpty() }
                            ?: getOwnerUseCase(),
                        items = pageOutput.bitmapDrawables.map { item ->
                            ApiGetItem(
                                id = item.id,
                                type = ItemType.IMG,
                                isDeleted = false,
                                content = ApiContent(
                                    typename = "img",
                                    onTextOutputType = null,
                                    onImgOutputType = ApiImg(
                                        noteId = unsentNotes.id,
                                        scale = item.scale,
                                        itemId = item.id,
                                    ),
                                    onPathOutputType = null,
                                ),
                                createdAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                createdBy = pageOutput.createdBy.takeIf { !isNullOrEmpty() }
                                    ?: getOwnerUseCase(),
                                modifiedAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                modifiedBy = pageOutput.createdBy.takeIf { !isNullOrEmpty() }
                                    ?: getOwnerUseCase(),
                                position = ApiPosition(
                                    posX = item.offsetX.toDouble(),
                                    posY = item.offsetY.toDouble(),
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
                                createdBy = pageOutput.createdBy.takeIf { !isNullOrEmpty() }
                                    ?: getOwnerUseCase(),
                                modifiedAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                modifiedBy = pageOutput.createdBy.takeIf { !isNullOrEmpty() }
                                    ?: getOwnerUseCase(),
                                position = null, hash = null
                            )
                        } + pageOutput.textDrawables.map { item ->
                            ApiGetItem(
                                id = item.id,
                                type = ItemType.MD,
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
                                createdBy = pageOutput.createdBy.takeIf { !isNullOrEmpty() }
                                    ?: getOwnerUseCase(),
                                modifiedAt = formatZonedDateTimeToIsoString(item.createdAt.zoneDateFromTimeStamp()),
                                modifiedBy = pageOutput.createdBy.takeIf { !isNullOrEmpty() }
                                    ?: getOwnerUseCase(),
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
                it.remoteId == remoteNote.noteId
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
                createdBy = remoteUnsavedNote.createdBy.takeIf { !isNullOrEmpty() }
                    ?: getOwnerUseCase(),
                createdAt = toTimestampDataType(remoteUnsavedNote.createdAt as String),
                modifiedAt = toTimestampDataType(remoteUnsavedNote.modifiedAt as String),
                pages = noteDetails.pages.map { page ->
                    PageOutputModel(
                        id = page.id!!,
                        createdAt = toTimestampDataType(page.createdAt as String),
                        createdBy = page.createdBy!!,
                        modifiedAt = toTimestampDataType(page.modifiedAt as String),
                        modifiedBy = page.modifiedBy!!,
                        bitmapDrawable = page.items?.filter { it?.type == ItemType.IMG }
                            ?.map { bitmap ->
                                BitmapDrawableModel(
                                    id = bitmap?.id ?: UUID.randomUUID().toString(),
                                    width = bitmap?.position?.width?.toInt() ?: 0,
                                    height = bitmap?.position?.height?.toInt() ?: 0,
                                    scale = bitmap?.content?.onImgOutputType?.scale ?: 1f,
                                    offsetX = bitmap?.position?.posX?.toFloat() ?: 0f,
                                    offsetY = bitmap?.position?.posY?.toFloat() ?: 0f,
                                    bitmap = "",//TODO bitmap ważne to dodać totalnie inaczej niż teraz to działa
                                    bitmapUrl = "",//TODO bitmap
                                    createdAt = toTimestampDataType(bitmap?.createdAt as String),
                                )
                            } ?: emptyList(),
                        pathDrawables = page.items?.filter { it?.type == ItemType.SVG }
                            ?.map { path ->
                                PathDrawableModel(
                                    id = path?.id ?: UUID.randomUUID().toString(),
                                    strokeWidth = path?.content?.onPathOutputType?.strokeWidth?.toFloat()
                                        ?: 0f,
                                    color = path?.content?.onPathOutputType?.color ?: "#FFF",
                                    alpha = path?.content?.onPathOutputType?.alpha?.toFloat()
                                        ?: 0f,
                                    eraseMode = path?.content?.onPathOutputType?.eraseMode
                                        ?: false,
                                    path = path?.content?.onPathOutputType?.path
                                        ?: "",
                                    createdAt = toTimestampDataType(path?.createdAt as String),
                                )
                            } ?: emptyList(),
                        textDrawables = page.items?.filter { it?.type == ItemType.MD }
                            ?.map { text ->
                                TextDrawableModel(
                                    id = text?.id ?: UUID.randomUUID().toString(),
                                    text = text?.content?.onTextOutputType?.text ?: "",
                                    color = text?.content?.onTextOutputType?.color ?: "",
                                    offsetX = text?.position?.posX?.toFloat() ?: 0f,
                                    offsetY = text?.position?.posY?.toFloat() ?: 0f,
                                    createdAt = toTimestampDataType(text?.createdAt as String),
                                )

                            } ?: emptyList(),
                    )
                }.takeIf { it.isNotEmpty() } ?: listOf(
                    //Zabezpiecznie przed tym by nie było pustej listy
                    PageOutputModel(
                        createdAt = System.currentTimeMillis(),
                        createdBy = getOwnerUseCase(),
                        modifiedAt = System.currentTimeMillis(),
                        modifiedBy = getOwnerUseCase(),
                    )
                ),
                quickNote = QuickNoteModel(id = "", text = "")
            )
        }
    }
}