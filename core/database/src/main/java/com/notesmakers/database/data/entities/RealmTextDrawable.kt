package com.notesmakers.database.data.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmTextDrawable() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var remoteItemId: String? = ""
    var text: String = ""
    var color: String = ""
    var offsetX: Float = 0f
    var offsetY: Float = 0f

    var createdAt: Long = 0L

    constructor(
        id: String = UUID.randomUUID().toString(),
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
        createdAt: Long = System.currentTimeMillis(),
    ) : this() {
        this.color = color
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.text = text
        this.id = id
        this.createdAt = createdAt
    }

}