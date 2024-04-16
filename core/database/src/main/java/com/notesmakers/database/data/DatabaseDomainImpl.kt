package com.notesmakers.database.data

import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.data.models.toNote
import com.notesmakers.database.data.models.toNoteData
import com.notesmakers.database.domain.DatabaseDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.getKoin

class DatabaseDomainImpl<Note>(
    private val noteTransformer: (DomainNoteModel) -> Note,
    private val toDataNoteTransformer: (Note) -> DomainNoteModel,
) : DatabaseDomain<Note> {
    override val notesDao: NotesDao by getKoin().inject<NotesDao>()
    override fun getNotes(): Flow<List<Note>> =
        notesDao.getNotes()
            .map { results -> results.map { noteTransformer(it.toNoteData()) }.toList() }

    override suspend fun deleteNote(id: String) = withContext(Dispatchers.IO) {
        notesDao.deleteNote(id)
    }

    override suspend fun updateNote(note: Note): Note? = withContext(Dispatchers.IO) {
        notesDao.updateNote(toDataNoteTransformer(note).toNote())?.toNoteData()
            ?.let { noteTransformer(it) }
    }

    override suspend fun insertNote(note: Note): Note = withContext(Dispatchers.IO) {
        noteTransformer(notesDao.insertNote(toDataNoteTransformer(note).toNote()).toNoteData())
    }
}