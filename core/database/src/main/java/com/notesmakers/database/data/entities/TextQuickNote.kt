package com.notesmakers.database.data.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class TextQuickNote() : RealmObject {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextQuickNote

        if (id != other.id) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }

}