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

    var notePageIndex = 0
    var createdAt: Long = 0L
    fun toDrawableComponentModel(): PathDrawableModel = PathDrawableModel(
        id = id,
        strokeWidth = strokeWidth,
        color = color,
        alpha = alpha,
        eraseMode = eraseMode,
        path = path,
        createdAt = createdAt,
        notePageIndex = notePageIndex
    )

    constructor(
        id: String = UUID.randomUUID().toString(),
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        createdAt: Long = System.currentTimeMillis(),
        notePageIndex: Int,
    ) : this() {
        this.strokeWidth = strokeWidth
        this.color = color
        this.id = id
        this.alpha = alpha
        this.eraseMode = eraseMode
        this.path = path
        this.createdAt = createdAt
        this.notePageIndex = notePageIndex
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
        if (createdAt != other.createdAt) return false
        if (notePageIndex != other.notePageIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + strokeWidth.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + alpha.hashCode()
        result = 31 * result + eraseMode.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + notePageIndex.hashCode()
        return result
    }

}