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
    var title: String = ""
    var ownerId: String = ""
    var description: String = ""
    var createdAt: Long = 0
    var pageCount: Int = 1

    var noteType: String = UNDEFINED
    var bitmapDrawables: RealmList<RealmBitmapDrawable> = realmListOf()
    var pathDrawables: RealmList<RealmPathDrawable> = realmListOf()
    var textDrawables: RealmList<RealmTextDrawable> = realmListOf()

    constructor(
        id: String = UUID.randomUUID().toString(),
        title: String,
        description: String,
        ownerId: String,
        pageCount: Int = 1,
        createdAt: Long = System.currentTimeMillis(),
        noteType: String = UNDEFINED,
        bitmapDrawable: RealmList<RealmBitmapDrawable> = realmListOf(),
        pathDrawables: RealmList<RealmPathDrawable> = realmListOf(),
        textDrawables: RealmList<RealmTextDrawable> = realmListOf(),
    ) : this() {
        this.title = title
        this.description = description
        this.id = id
        this.ownerId = ownerId
        this.noteType = noteType
        this.bitmapDrawables = bitmapDrawable
        this.pathDrawables = pathDrawables
        this.textDrawables = textDrawables
        this.createdAt = createdAt
        this.pageCount = pageCount
    }
}
