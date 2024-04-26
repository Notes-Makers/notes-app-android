package com.notesmakers.database.data.entities


import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmPageOutput() : RealmObject {

    @PrimaryKey
    var id: String = ""

    var createdAt: Long = 0
    var createdBy: String = ""
    var modifiedAt: Long = 0
    var modifiedBy: String = ""

    var bitmapDrawables: RealmList<RealmBitmapDrawable> = realmListOf()
    var pathDrawables: RealmList<RealmPathDrawable> = realmListOf()
    var textDrawables: RealmList<RealmTextDrawable> = realmListOf()

    constructor(
        id: String = UUID.randomUUID().toString(),
        createdBy: String,
        modifiedBy: String = createdBy,
        createdAt: Long = System.currentTimeMillis(),
        modifiedAt: Long = System.currentTimeMillis(),
        bitmapDrawable: RealmList<RealmBitmapDrawable> = realmListOf(),
        pathDrawables: RealmList<RealmPathDrawable> = realmListOf(),
        textDrawables: RealmList<RealmTextDrawable> = realmListOf(),
    ) : this() {
        this.id = id
        this.bitmapDrawables = bitmapDrawable
        this.pathDrawables = pathDrawables
        this.textDrawables = textDrawables
        this.createdAt = createdAt
        this.modifiedAt = modifiedAt
        this.modifiedBy = modifiedBy
        this.createdBy = createdBy
    }
}
