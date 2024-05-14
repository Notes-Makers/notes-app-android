package com.notesmakers.network.data.api

data class ApiContent(
    val typename: String?,
    val onTextOutputType: ApiText?,
    val onImgOutputType: ApiImg?,
    val onPathOutputType: ApiPath?,
)