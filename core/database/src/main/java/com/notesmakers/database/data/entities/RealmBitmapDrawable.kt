package com.notesmakers.database.data.entities

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmBitmapDrawable() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var width: Int = 0
    var height: Int = 0
    var scale: Float = 1f
    var offsetX: Float = 0f
    var offsetY: Float = 0f
    var bitmap: String = ""
    var bitmapUrl: String = ""

    var createdAt: Long = 0

    constructor(
        id: String = UUID.randomUUID().toString(),
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        createdAt: Long = System.currentTimeMillis(),
        bitmapUrl: String,
    ) : this() {
        this.id = id
        this.width = width
        this.height = height
        this.scale = scale
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.bitmap = bitmap
        this.createdAt = createdAt
        this.bitmapUrl = bitmapUrl
    }
}