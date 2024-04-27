package com.notesmakers.database.data.dao

import com.notesmakers.database.data.entities.RealmBitmapDrawable
import com.notesmakers.database.data.entities.RealmNote
import com.notesmakers.database.data.entities.RealmPageOutput
import com.notesmakers.database.data.entities.RealmPathDrawable
import com.notesmakers.database.data.entities.RealmTextDrawable
import com.notesmakers.database.data.entities.RealmQuickNote
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
        name: String,
        description: String,
        createdBy: String,
        noteType: String = UNDEFINED,
    ) = realm.write {
        copyToRealm(
            RealmNote(
                name = name,
                description = description,
                createdBy = createdBy,
                noteType = noteType
            ), updatePolicy = UpdatePolicy.ALL
        )
    }

    suspend fun addTextDrawableToNote(
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ) = realm.write {

        val findRealmNote = query<RealmPageOutput>("id == $0", pageId).first().find()

        findRealmNote?.apply {
            textDrawables.add(
                RealmTextDrawable(
                    text = text,
                    color = color,
                    offsetX = offsetX,
                    offsetY = offsetY,
                )
            )
        }
    }

    suspend fun addBitmapDrawableToNote(
        pageId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        bitmapUrl: String,
    ) = realm.write {
        val findRealmNote = query<RealmPageOutput>("id == $0", pageId).first().find()
        findRealmNote?.apply {
            bitmapDrawables.add(
                RealmBitmapDrawable(
                    width = width,
                    height = height,
                    scale = scale,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    bitmap = bitmap,
                    bitmapUrl = bitmapUrl
                )
            )
        }
    }

    suspend fun addPathDrawableToNote(
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
    ) = realm.write {
        val findRealmNote = query<RealmPageOutput>("id == $0", pageId).first().find()
        findRealmNote?.apply {
            pathDrawables.add(
                RealmPathDrawable(
                    strokeWidth = strokeWidth,
                    color = color,
                    alpha = alpha,
                    eraseMode = eraseMode,
                    path = path,
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
        name: String?,
        description: String?,
        modifiedBy: String?,
    ) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            if (name != null) {
                this.name = name
            }
            if (description != null) {
                this.description = description
            }
            if (modifiedBy != null) {
                this.modifiedBy = modifiedBy
            }
        }
    }

    suspend fun updatePageNote(noteId: String, createdBy: String) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()

        findRealmNote?.apply {
            this.pages.add(
                RealmPageOutput(
                    createdBy = createdBy
                )
            )
        }
    }

    suspend fun updateTextNote(noteId: String, text: String) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()
        findRealmNote?.apply {
            this.realmQuickNote = RealmQuickNote(text = text)
        }
    }

    suspend fun updatePinned(noteId: String, isPinned: Boolean) = realm.write {
        val findRealmNote = query<RealmNote>("id == $0", noteId).first().find()
        findRealmNote?.apply {
            this.isPinned = isPinned
        }
    }
}