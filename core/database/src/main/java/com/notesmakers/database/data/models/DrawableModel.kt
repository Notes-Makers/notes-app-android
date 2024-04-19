package com.notesmakers.database.data.models


interface DrawableModel<T> {
    fun toRealmDrawableComponent(): T
}