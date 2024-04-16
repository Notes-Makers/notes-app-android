package com.notesmakers.database.data.entities

import com.notesmakers.database.data.models.TextDrawableModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmTextDrawable() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var text: String = ""
    var color: Long = 0xFF3B82F6
    var offsetX: Float = 0f
    var offsetY: Float = 0f

    var timestamp: Long = 0L
    fun toDrawableComponentModel(): TextDrawableModel = TextDrawableModel(
        id = id,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
        timestamp = timestamp,
    )

    constructor(
        id: String = UUID.randomUUID().toString(),
        text: String,
        color: Long,
        offsetX: Float,
        offsetY: Float,
        timestamp: Long,
    ) : this() {
        this.color = color
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.text = text
        this.id = id
        this.timestamp = timestamp
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RealmTextDrawable

        if (id != other.id) return false
        if (text != other.text) return false
        if (color != other.color) return false
        if (offsetX != other.offsetX) return false
        if (offsetY != other.offsetY) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + offsetX.hashCode()
        result = 31 * result + offsetY.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }


}