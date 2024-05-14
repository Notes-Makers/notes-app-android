package com.notesmakers.database.data.entities


import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

const val UNDEFINED = "UNDEFINED"

class RealmNote() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var remoteNoteId: String? = ""
    var name: String = ""
    var description: String = ""
    var pages: RealmList<RealmPageOutput> = realmListOf()
    var createdAt: Long = 0
    var createdBy: String = ""
    var modifiedAt: Long = 0
    var modifiedBy: String = ""
    var isPrivate: Boolean = true
    var isShared: Boolean = false
    var tag: RealmList<String> = realmListOf()

    var noteType: String = UNDEFINED
    var isPinned: Boolean = false
    var realmQuickNote: RealmQuickNote? = RealmQuickNote()

    constructor(
        id: String = UUID.randomUUID().toString(),
        remoteNoteId: String? = "",
        name: String,
        description: String,
        noteType: String,
        createdAt: Long = System.currentTimeMillis(),
        createdBy: String = "",
        pages: RealmList<RealmPageOutput> = realmListOf(RealmPageOutput(createdBy = createdBy)),
        modifiedBy: String = createdBy,
        modifiedAt: Long = System.currentTimeMillis(),
        isPrivate: Boolean = false,
        isShared: Boolean = false,
        isPinned: Boolean = false,
        tag: RealmList<String> = realmListOf(),
        realmQuickNote: RealmQuickNote = RealmQuickNote(),
    ) : this() {
        this.id = id
        this.remoteNoteId = remoteNoteId
        this.name = name
        this.description = description
        this.pages = pages
        this.createdBy = createdBy
        this.noteType = noteType
        this.modifiedAt = modifiedAt
        this.modifiedBy = modifiedBy
        this.isPrivate = isPrivate
        this.createdAt = createdAt
        this.isShared = isShared
        this.isPinned = isPinned
        this.tag = tag
        this.realmQuickNote = realmQuickNote
    }
}
