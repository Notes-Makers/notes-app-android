package com.notesmakers.network.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.notesmakers.network.AddItemMutation
import com.notesmakers.network.AddPageMutation
import com.notesmakers.network.CreateNoteMutation
import com.notesmakers.network.DeleteItemMutation
import com.notesmakers.network.DeleteNoteMutation
import com.notesmakers.network.DeletePageMutation
import com.notesmakers.network.GetItemQuery
import com.notesmakers.network.GetItemsInfoQuery
import com.notesmakers.network.GetNoteQuery
import com.notesmakers.network.GetNotesInfoQuery
import com.notesmakers.network.GetPageQuery
import com.notesmakers.network.GetPagesInfoQuery
import com.notesmakers.network.UpdateNoteMutation
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiNoteType
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.domain.NetworkClient
import com.notesmakers.network.type.ImgInputType
import com.notesmakers.network.type.ItemInput
import com.notesmakers.network.type.ItemType
import com.notesmakers.network.type.PageInput
import com.notesmakers.network.type.PathInputType
import com.notesmakers.network.type.PositionInput
import com.notesmakers.network.type.TextInputType

class ApolloNetworkClient(
    private val apolloClient: ApolloClient,
) : NetworkClient {
    override suspend fun addItem(
        noteId: String,
        pageId: String,
        itemId: String,
        itemType: ItemType,
        imgContent: ApiImg?,
        pathContent: ApiPath?,
        textContent: ApiText?,
        itemPosX: Double,
        itemPosY: Double,
        itemWidth: Double,
        itemHeight: Double,
        itemCreatedAt: String,
        itemCreatedBy: String,
        itemModifiedAt: String,
        itemModifiedBy: String,
        itemHash: String
    ) = apolloClient.mutation(
        AddItemMutation(
            noteId = noteId,
            pageId = pageId,
            itemId = itemId,
            itemType = itemType,
            imgContent = Optional.presentIfNotNull(imgContent.takeIf { it != null }?.let {
                ImgInputType(
                    noteId = Optional.present(imgContent?.noteId),
                    itemId = Optional.present(imgContent?.itemId),
                    scale = Optional.present(imgContent?.scale?.toDouble()),
                )
            }),
            pathContent = Optional.presentIfNotNull(pathContent.takeIf { it != null }?.let {
                PathInputType(
                    strokeWidth = Optional.present(pathContent?.strokeWidth),
                    color = Optional.present(pathContent?.color),
                    alpha = Optional.present(pathContent?.alpha),
                    eraseMode = Optional.present(pathContent?.eraseMode),
                    path = Optional.present(pathContent?.path),
                )
            }),
            textContent = Optional.presentIfNotNull(textContent.takeIf { it != null }?.let {
                TextInputType(
                    text = Optional.present(textContent?.text),
                    color = Optional.present(textContent?.color),
                )
            }),
            itemPosX = itemPosX,
            itemPosY = itemPosY,
            itemWidth = itemWidth,
            itemHeight = itemHeight,
            itemCreatedAt = itemCreatedAt,
            itemCreatedBy = itemCreatedBy,
            itemModifiedAt = itemModifiedAt,
            itemModifiedBy = itemModifiedBy,
            itemHash = itemHash
        )
    ).execute()


    override suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String,
    ) = apolloClient.mutation(
        AddPageMutation(
            noteId = noteId,
            pageId = pageId,
            createdAt = createdAt,
            createdBy = createdBy,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy
        )
    ).execute()

    override suspend fun addNote(
        name: String,
        type: ApiNoteType,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String,
        pages: List<ApiGetPage>
    ) =
        apolloClient.mutation(
            CreateNoteMutation(
                name = name,
                type = type.toNoteType(),
                description = description,
                createdBy = createdBy,
                isShared = isShared,
                createdAt = createdAt,
                isPrivate = isPrivate,
                modifiedAt = modifiedAt,
                modifiedBy = modifiedBy,
                pages = Optional.present(pages.map {
                    PageInput(
                        id = Optional.present(it.id),
                        createdAt = Optional.present(it.createdAt),
                        createdBy = Optional.present(it.createdBy),
                        modifiedAt = Optional.present(it.modifiedAt),
                        modifiedBy = Optional.present(it.modifiedBy),
                        items = Optional.present(it.items?.map {
                            ItemInput(
                                id = Optional.presentIfNotNull(it?.id),
                                type = Optional.presentIfNotNull(it?.type),
                                imgContent = Optional.presentIfNotNull(it.takeIf { it != null }
                                    ?.let {
                                        ImgInputType(
                                            noteId = Optional.presentIfNotNull(
                                                it.content?.onImgOutputType?.noteId
                                            ),
                                            itemId = Optional.presentIfNotNull(it.content?.onImgOutputType?.itemId),
                                            scale = Optional.presentIfNotNull(it.content?.onImgOutputType?.scale?.toDouble()),
                                        )
                                    }),
                                pathContent = Optional.presentIfNotNull(it.takeIf { it != null }
                                    ?.let {
                                        PathInputType(
                                            strokeWidth = Optional.present(it.content?.onPathOutputType?.strokeWidth),
                                            color = Optional.present(it.content?.onPathOutputType?.color),
                                            alpha = Optional.present(it.content?.onPathOutputType?.alpha),
                                            eraseMode = Optional.present(it.content?.onPathOutputType?.eraseMode),
                                            path = Optional.present(it.content?.onPathOutputType?.path),
                                        )
                                    }),
                                textContent = Optional.presentIfNotNull(it.takeIf { it != null }
                                    ?.let {
                                        TextInputType(
                                            text = Optional.present(it.content?.onTextOutputType?.text),
                                            color = Optional.present(it.content?.onTextOutputType?.color),
                                        )
                                    }),
                                position = Optional.presentIfNotNull(it.takeIf { it != null }?.let {
                                    PositionInput(
                                        posX = Optional.presentIfNotNull(it.position?.posX),
                                        posY = Optional.presentIfNotNull(it.position?.posY),
                                        width = Optional.presentIfNotNull(it.position?.width),
                                        height = Optional.presentIfNotNull(it.position?.height),
                                    )
                                }),
                                createdAt = Optional.presentIfNotNull(it?.createdAt),
                                createdBy = Optional.presentIfNotNull(it?.createdBy),
                                modifiedAt = Optional.presentIfNotNull(it?.modifiedAt),
                                modifiedBy = Optional.presentIfNotNull(it?.modifiedBy),
                            )
                        })
                    )
                })
            ),
        ).execute()

    override suspend fun deleteItem(noteId: String, pageId: String, itemId: String) =
        apolloClient.mutation(
            DeleteItemMutation(
                noteId = noteId,
                pageId = pageId,
                itemId = itemId
            ),
        ).execute()


    override suspend fun deleteNote(noteId: String) =
        apolloClient.mutation(
            DeleteNoteMutation(
                noteId = noteId
            )
        ).execute()


    override suspend fun deletePage(noteId: String, pageId: String) =
        apolloClient.mutation(
            DeletePageMutation(
                noteId = noteId,
                pageId = pageId,
            )
        ).execute()


    override suspend fun getItem(noteId: String, pageId: String, itemId: String) =
        apolloClient.query(
            GetItemQuery(
                noteId = noteId,
                pageId = pageId,
                itemId = itemId,
            )
        ).execute()


    override suspend fun getItemsInfo(noteId: String, pageId: String) =
        apolloClient.query(
            GetItemsInfoQuery(
                noteId = noteId,
                pageId = pageId,
            )
        ).execute()


    override suspend fun getNote(noteId: String) =
        apolloClient.query(
            GetNoteQuery(
                noteId = noteId,
            )
        ).execute()


    override suspend fun getNotesInfo() =
        apolloClient.query(
            GetNotesInfoQuery()
        ).execute()


    override suspend fun getPage(noteId: String, pageId: String) =
        apolloClient.query(
            GetPageQuery(
                noteId = noteId,
                pageId = pageId,
            )
        ).execute()


    override suspend fun getPagesInfo(noteId: String) =
        apolloClient.query(
            GetPagesInfoQuery(
                noteId = noteId,
            )
        ).execute()

    suspend fun updateNote(
        noteId: String,
        name: String,
        description: String,
    ) = apolloClient.mutation(
        UpdateNoteMutation(
            noteId = noteId,
            description = description,
            title = name,
        )
    ).execute()
}
