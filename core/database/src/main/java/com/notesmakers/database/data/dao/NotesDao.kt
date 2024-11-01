package com.notesmakers.database.data.dao

import com.notesmakers.database.data.entities.RealmBitmapDrawable
import com.notesmakers.database.data.entities.RealmNote
import com.notesmakers.database.data.entities.RealmPageOutput
import com.notesmakers.database.data.entities.RealmPathDrawable
import com.notesmakers.database.data.entities.RealmTextDrawable
import com.notesmakers.database.data.entities.UNDEFINED
import com.notesmakers.database.data.models.PageOutputModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import java.util.UUID

@Factory
class NotesDao(
    private val realm: Realm
) {
    suspend fun createNote(
        name: String,
        description: String,
        createdBy: String,
        noteType: String = UNDEFINED,
    ) = realm.write {
        copyToRealm(
            RealmNote(
                name = name, description = description, createdBy = createdBy, noteType = noteType
            ), updatePolicy = UpdatePolicy.ALL
        )
    }

    suspend fun createCompleteNote(
        remoteNoteId: String,
        name: String,
        description: String,
        noteType: String,
        createdAt: Long = System.currentTimeMillis(),
        createdBy: String = "",
        pages: List<PageOutputModel>,
        modifiedBy: String = createdBy,
        modifiedAt: Long = System.currentTimeMillis(),
        isPrivate: Boolean = false,
        isShared: Boolean = false,
        isPinned: Boolean = false,
        tag: List<String> = listOf(),
    ) = realm.write {
        copyToRealm(
            RealmNote(
                remoteNoteId = remoteNoteId,
                name = name,
                description = description,
                createdBy = createdBy,
                noteType = noteType,
                modifiedBy = modifiedBy,
                createdAt = createdAt,
                modifiedAt = modifiedAt,
                isPrivate = isPrivate,
                isShared = isShared,
                isPinned = isPinned,
                tag = tag.toRealmList(),
                pages = pages.map { page ->
                    RealmPageOutput(
                        id = page.id,
                        createdBy = page.createdBy,
                        modifiedBy = page.modifiedBy,
                        createdAt = page.createdAt,
                        modifiedAt = page.modifiedAt,
                        bitmapDrawable = page.bitmapDrawable.map { bitmap ->
                            RealmBitmapDrawable(
                                id = bitmap.id,
                                width = bitmap.width,
                                height = bitmap.height,
                                scale = bitmap.scale,
                                offsetX = bitmap.offsetX,
                                offsetY = bitmap.offsetY,
                                bitmap = bitmap.bitmap,
                                createdAt = bitmap.createdAt,
                                bitmapUrl = bitmap.bitmapUrl,
                            )
                        }.toRealmList(),
                        pathDrawables = page.pathDrawables.map { path ->
                            RealmPathDrawable(
                                id = path.id,
                                strokeWidth = path.strokeWidth,
                                color = path.color,
                                alpha = path.alpha,
                                eraseMode = path.eraseMode,
                                path = path.path,
                            )
                        }.toRealmList(),
                        textDrawables = page.textDrawables.map { text ->
                            RealmTextDrawable(
                                id = text.id,
                                text = text.text,
                                color = text.color,
                                offsetX = text.offsetX,
                                offsetY = text.offsetY,
                                createdAt = text.createdAt,
                            )

                        }.toRealmList(),
                    )
                }.toRealmList()
            ), updatePolicy = UpdatePolicy.ALL
        )
    }

    suspend fun addTextDrawableToNote(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
        createdAt: Long = System.currentTimeMillis(),
    ) = realm.write {

        val findRealmNote = query<RealmPageOutput>("id == $0", pageId).first().find()

        findRealmNote?.apply {
            modifiedAt = createdAt
            textDrawables.add(
                RealmTextDrawable(
                    id = id,
                    text = text,
                    color = color,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    createdAt = createdAt
                )
            )
        }
        val realmNote = query<RealmNote>("id == $0", noteId).first().find()
        realmNote?.apply {
            modifiedAt = createdAt
        }
    }

    suspend fun addBitmapDrawableToNote(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        pageId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        bitmapUrl: String,
        createdAt: Long = System.currentTimeMillis(),
    ) = realm.write {
        val findRealmNote = query<RealmPageOutput>("id == $0", pageId).first().find()
        findRealmNote?.apply {
            modifiedAt = createdAt
            bitmapDrawables.add(
                RealmBitmapDrawable(
                    id = id,
                    width = width,
                    height = height,
                    scale = scale,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    bitmap = bitmap,
                    bitmapUrl = bitmapUrl,
                    createdAt = createdAt
                )
            )
        }
        val realmNote = query<RealmNote>("id == $0", noteId).first().find()
        realmNote?.apply {
            modifiedAt = createdAt
        }
    }

    suspend fun addPathDrawableToNote(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        createdAt: Long = System.currentTimeMillis(),
    ) = realm.write {
        val findRealmNote = query<RealmPageOutput>("id == $0", pageId).first().find()
        findRealmNote?.apply {
            modifiedAt = createdAt
            pathDrawables.add(
                RealmPathDrawable(
                    id = id,
                    strokeWidth = strokeWidth,
                    color = color,
                    alpha = alpha,
                    eraseMode = eraseMode,
                    path = path,
                    createdAt = createdAt,
                )
            )
        }
        val realmNote = query<RealmNote>("id == $0", noteId).first().find()
        realmNote?.apply {
            modifiedAt = createdAt
        }
    }

    fun getNotes(): Flow<List<RealmNote>> =
        realm.query<RealmNote>().asFlow().map { results -> results.list.toList() }

    fun getNoteById(noteId: String): RealmNote? =
        realm.query<RealmNote>("id == $0", noteId).first().find()

    suspend fun deleteNote(noteId: String) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", noteId).find()
        val tmpListOfPage = findRealmNote.first().pages.map { it.id }
        delete(findRealmNote)
        tmpListOfPage.forEach { id ->
            val page = query<RealmPageOutput>("id == $0", id).find().first()
            val tmpListOfText = page.textDrawables.map { it.id }
            val tmpListOfBitmap = page.bitmapDrawables.map { it.id }
            val tmpListOfPath = page.pathDrawables.map { it.id }
            delete(page)
            if (tmpListOfPath.isNotEmpty()) {
                tmpListOfPath.forEach { path ->
                    val pathDrawable =
                        query<RealmPathDrawable>("id == $0", path).find().takeIf { it.isNotEmpty() }
                            ?.first()
                    if (pathDrawable != null) {
                        delete(pathDrawable)
                    }
                }
            }
            if (tmpListOfBitmap.isNotEmpty()) {
                tmpListOfBitmap.forEach { bitmap ->
                    val bitmapDrawable =
                        query<RealmBitmapDrawable>("id == $0", bitmap).find()
                            .takeIf { it.isNotEmpty() }?.first()
                    if (bitmapDrawable != null) {
                        delete(bitmapDrawable)
                    }
                }
            }
            if (tmpListOfText.isNotEmpty()) {
                tmpListOfText.forEach { text ->
                    val textDrawable =
                        query<RealmTextDrawable>("id == $0", text).find().takeIf { it.isNotEmpty() }
                            ?.first()
                    if (textDrawable != null) {
                        delete(textDrawable)
                    }
                }
            }

        }
    }


    suspend fun updateNote(
        noteId: String?,
        name: String?,
        description: String?,
        modifiedAt: Long?,
    ) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            if (name != null) {
                this.name = name
            }
            if (description != null) {
                this.description = description
            }
            this.modifiedAt = modifiedAt ?: System.currentTimeMillis()
        }
    }

    suspend fun updateRemoteNoteId(
        noteId: String, remoteNoteId: String?
    ) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            if (remoteNoteId != null) {
                this.remoteNoteId = remoteNoteId
            }
        }
    }

    suspend fun updatePageNote(noteId: String, createdBy: String) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            this.modifiedAt = System.currentTimeMillis()
            this.pages.add(
                RealmPageOutput(
                    createdBy = createdBy
                )
            )
        }
    }

    suspend fun updatePageNote(
        noteId: String,
        pageId: String,
        createdBy: String,
        createdAt: Long,
        modifiedBy: String,
        modifiedAt: Long,
    ) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            this.modifiedAt = modifiedAt
            this.pages.add(
                RealmPageOutput(
                    id = pageId,
                    createdBy = createdBy,
                    modifiedBy = modifiedBy,
                    createdAt = createdAt,
                    modifiedAt = modifiedAt,
                )
            )
        }
    }

    suspend fun updateTextNote(noteId: String, text: String, modifiedAt: Long?) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()
        findRealmNote?.apply {
            this.pages.firstOrNull()?.let { page ->
                if (page.textDrawables.isNotEmpty()) {
                    val findedText =
                        query<RealmTextDrawable>("id == $0", page.textDrawables.first().id).first()
                            .find()
                    findedText?.apply {
                        this.text = text
                        this.createdAt = modifiedAt ?: System.currentTimeMillis()
                    }
                } else {
                    page.textDrawables.add(
                        RealmTextDrawable(
                            id = UUID.randomUUID().toString(),
                            text = text,
                            color = "#3DDC84",
                            offsetX = 0f,
                            offsetY = 0f,
                            createdAt = modifiedAt ?: System.currentTimeMillis()
                        )
                    )
                }

            }
        }
        findRealmNote?.pages?.firstOrNull()?.textDrawables?.firstOrNull()?.text
    }

    suspend fun updatePinned(noteId: String, isPinned: Boolean) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()
        findRealmNote?.apply {
            this.isPinned = isPinned
        }
    }
}