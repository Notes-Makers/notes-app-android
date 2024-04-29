package com.notesmakers.network.data.api

class ApiGetPage(
    val id: String?,
    val isDeleted: Boolean?,
    val createdAt: Any?,
    val createdBy: String?,
    val modifiedAt: Any?,
    val modifiedBy: String?,
    val items: List<ApiGetItem?>?,
)