package com.notesmakers.database.data.entities

import com.notesmakers.database.data.models.BitmapDrawableModel
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

    var notePageIndex = 0
    var createdAt: Long = 0
    fun toDrawableComponentModel(): BitmapDrawableModel = BitmapDrawableModel(
        id = id,
        width = width,
        height = height,
        scale = scale,
        offsetX = offsetX,
        offsetY = offsetY,
        bitmap = bitmap,
        createdAt = createdAt,
        notePageIndex = notePageIndex
    )

    constructor(
        id: String = UUID.randomUUID().toString(),
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        createdAt: Long= System.currentTimeMillis(),
        notePageIndex: Int,
    ) : this() {
        this.id = id
        this.width = width
        this.height = height
        this.scale = scale
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.bitmap = bitmap
        this.createdAt = createdAt
        this.notePageIndex = notePageIndex
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RealmBitmapDrawable

        if (id != other.id) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (scale != other.scale) return false
        if (offsetX != other.offsetX) return false
        if (offsetY != other.offsetY) return false
        if (bitmap != other.bitmap) return false
        if (notePageIndex != other.notePageIndex) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + scale.hashCode()
        result = 31 * result + offsetX.hashCode()
        result = 31 * result + offsetY.hashCode()
        result = 31 * result + bitmap.hashCode()
        result = 31 * result + notePageIndex.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }

}