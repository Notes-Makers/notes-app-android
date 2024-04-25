package com.notesmakers.database.data.dao

import com.notesmakers.database.data.entities.RealmBitmapDrawable
import com.notesmakers.database.data.entities.RealmNote
import com.notesmakers.database.data.entities.RealmPathDrawable
import com.notesmakers.database.data.entities.RealmTextDrawable
import com.notesmakers.database.data.entities.TextQuickNote
import com.notesmakers.database.data.entities.UNDEFINED
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class NotesDao(
    private val realm: Realm
) {
    suspend fun createNote(
        title: String,
        description: String,
        ownerId: String,
        noteType: String = UNDEFINED,
    ) = realm.write {
        copyToRealm(
            RealmNote(
                title = title, description = description, ownerId = ownerId, noteType = noteType
            ), updatePolicy = UpdatePolicy.ALL
        )
    }

    suspend fun addTextDrawableToNote(
        noteId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
        notePageIndex: Int,
    ) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            textDrawables.add(
                RealmTextDrawable(
                    text = text,
                    color = color,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    notePageIndex = notePageIndex,
                )
            )
        }
    }

    suspend fun addBitmapDrawableToNote(
        noteId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        notePageIndex: Int,
    ) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()
        findRealmNote?.apply {
            bitmapDrawables.add(
                RealmBitmapDrawable(
                    width = width,
                    height = height,
                    scale = scale,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    bitmap = bitmap,
                    notePageIndex = notePageIndex
                )
            )
        }
    }

    suspend fun addPathDrawableToNote(
        noteId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        notePageIndex: Int,
    ) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()
        findRealmNote?.apply {
            pathDrawables.add(
                RealmPathDrawable(
                    strokeWidth = strokeWidth,
                    color = color,
                    alpha = alpha,
                    eraseMode = eraseMode,
                    path = path,
                    notePageIndex = notePageIndex
                )
            )
        }
    }

    fun getNotes(): Flow<List<RealmNote>> =
        realm.query<RealmNote>().asFlow().map { results -> results.list.toList() }

    fun getNoteById(noteId: String): RealmNote? =
        realm.query<RealmNote>("id == $0", noteId).first().find()

    suspend fun deleteNote(noteId: String) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", noteId).find()
        delete(findRealmNote)
    }

    suspend fun updateNote(
        noteId: String?,
        title: String?,
        description: String?,
        ownerId: String?,
    ) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            if (title != null) {
                this.title = title
            }
            if (description != null) {
                this.description = description
            }
            if (ownerId != null) {
                this.ownerId = ownerId
            }
        }
    }

    suspend fun updatePageNote(noteId: String, pageCount: Int) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            this.pageCount = pageCount
        }
    }

    suspend fun updateTextNote(noteId: String, text: String) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()
        findRealmNote?.apply {
            this.textQuickNote = TextQuickNote(text = text)
        }
    }
}