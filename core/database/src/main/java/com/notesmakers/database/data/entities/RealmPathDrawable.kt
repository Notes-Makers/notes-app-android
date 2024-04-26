package com.notesmakers.database.data.entities

import com.notesmakers.database.data.models.PathDrawableModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmPathDrawable() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var strokeWidth: Float = 5f
    var color: String = ""
    var alpha: Float = 1f
    var eraseMode: Boolean = false
    var path: String = ""

    var createdAt: Long = 0L

    constructor(
        id: String = UUID.randomUUID().toString(),
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        createdAt: Long = System.currentTimeMillis(),
    ) : this() {
        this.strokeWidth = strokeWidth
        this.color = color
        this.id = id
        this.alpha = alpha
        this.eraseMode = eraseMode
        this.path = path
        this.createdAt = createdAt
    }

}