package com.notesmakers.database.data.entities

import com.notesmakers.database.data.models.PathDrawableModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class RealmPathDrawable() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var strokeWidth: Float = 5f
    var color: Long = 0xFF3B82F6
    var alpha: Float = 1f
    var eraseMode: Boolean = false
    var path: String = ""

    var timestamp: Long = 0L
    fun toDrawableComponentModel(): PathDrawableModel = PathDrawableModel(
        id = id,
        strokeWidth = strokeWidth,
        color = color,
        alpha = alpha,
        eraseMode = eraseMode,
        path = path,
        timestamp = timestamp
    )

    constructor(
        id: String = UUID.randomUUID().toString(),
        strokeWidth: Float,
        color: Long,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        timestamp: Long,
    ) : this() {
        this.strokeWidth = strokeWidth
        this.color = color
        this.id = id
        this.alpha = alpha
        this.eraseMode = eraseMode
        this.path = path
        this.timestamp = timestamp
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RealmPathDrawable

        if (id != other.id) return false
        if (strokeWidth != other.strokeWidth) return false
        if (color != other.color) return false
        if (alpha != other.alpha) return false
        if (eraseMode != other.eraseMode) return false
        if (path != other.path) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + strokeWidth.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + eraseMode.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }

}