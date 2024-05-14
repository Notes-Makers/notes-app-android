package com.notesmakers.network.data.api

import com.notesmakers.network.type.ItemType

data class ApiGetItem(
    val id: String?,
    val type: ItemType?,
    val isDeleted: Boolean?,
    val content: ApiContent?,
    val createdAt: Any?,
    val createdBy: String?,
    val modifiedAt: Any?,
    val modifiedBy: String?,
    val hash: String?,
    val position: ApiPosition? = null
)