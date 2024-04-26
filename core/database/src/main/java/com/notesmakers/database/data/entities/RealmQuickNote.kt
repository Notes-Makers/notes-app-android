package com.notesmakers.database.data.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmQuickNote() : RealmObject {
    @PrimaryKey
    var id: String = ""
    var text: String = ""

    constructor(
        id: String = UUID.randomUUID().toString(),
        text: String
    ) : this() {
        this.id = id
        this.text = text
    }
}