package com.notesmakers.database.data.entities


import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmNote() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var bitmapDrawable: RealmList<RealmBitmapDrawable> = realmListOf()
    var pathDrawables: RealmList<RealmPathDrawable> = realmListOf()
    var textDrawables: RealmList<RealmTextDrawable> = realmListOf()

    constructor(
        id: String = UUID.randomUUID().toString(),
        title: String,
        description: String,
        bitmapDrawable: RealmList<RealmBitmapDrawable> = realmListOf(),
        pathDrawables: RealmList<RealmPathDrawable> = realmListOf(),
        textDrawables: RealmList<RealmTextDrawable> = realmListOf(),
    ) : this() {
        this.title = title
        this.description = description
        this.id = id
        this.bitmapDrawable = bitmapDrawable
        this.pathDrawables = pathDrawables
        this.textDrawables = textDrawables
    }
}
