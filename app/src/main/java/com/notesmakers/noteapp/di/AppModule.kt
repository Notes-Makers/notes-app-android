package com.notesmakers.noteapp.di

import com.notesmakers.database.domain.DatabaseDomain
import com.notesmakers.network.domain.NetworkDomain
import com.notesmakers.noteapp.data.notes.api.BaseItem
import com.notesmakers.noteapp.data.notes.api.BaseItemsInfo
import com.notesmakers.noteapp.data.notes.api.BaseNote
import com.notesmakers.noteapp.data.notes.api.BaseNotesInfo
import com.notesmakers.noteapp.data.notes.api.BasePage
import com.notesmakers.noteapp.data.notes.api.BasePagesInfo
import com.notesmakers.noteapp.data.notes.local.Note
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan("com.notesmakers.noteapp")
class AppModule {

    @Factory
    fun provideDispatcher() = Dispatchers.IO

    @Singleton
    fun createDatabaseDomain(): DatabaseDomain<Note> =
        DatabaseDomain.createDatabaseDomain(
            noteTransformer = {
                it.toNote()
            },
        )

    @Singleton
    fun createNetworkDomain(): NetworkDomain<BaseNote, BaseNotesInfo, BaseItem, BaseItemsInfo, BasePage, BasePagesInfo> =
        NetworkDomain.createNetworkDomain(
            noteTransformer = {
                it.toBaseNote()
            },
            notesInfoTransformer = {
                it.toBaseNotesInfo()
            },
            itemTransformer = {
                it.toBaseItem()
            },
            itemsInfoTransformer = {
                it.toBaseItemsInfo()
            },
            pageTransformer = {
                it.toBasePage()
            },
            pagesInfoTransformer = {
                it.toBasePagesInfo()
            }
        )
}

typealias DatabaseDomainModule = DatabaseDomain<Note>
typealias NotesNetworkDomainModule = NetworkDomain<BaseNote, BaseNotesInfo, BaseItem, BaseItemsInfo, BasePage, BasePagesInfo>