package com.notesmakers.database.data.dao

import com.notesmakers.database.data.entities.RealmNote
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
    suspend fun insertNote(notes: RealmNote) = realm.write {
        copyToRealm(notes, updatePolicy = UpdatePolicy.ALL)
    }

    // fetch all objects of a type as a flow, asynchronously
    fun getNotes(): Flow<List<RealmNote>> =
        realm.query<RealmNote>().asFlow().map { results -> results.list.toList() }

    suspend fun deleteNote(id: String) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", id).find()
        delete(findRealmNote)
    }

    suspend fun updateNote(notes: RealmNote?) = realm.write {

        val findRealmNote = query<RealmNote>("id == $0", notes?.id ?: "0").first().find()

        findRealmNote?.apply {
            title = notes?.title ?: "-"
            description = notes?.description ?: "-"
        }
    }

}