package com.example.notebook.data

import com.example.notebook.notes.NotesSortBy
import kotlinx.coroutines.flow.Flow

class NotesRepo(
    private val dao: NotesDao
): INotesRepo {
    override fun getNoteEntities(
        sortBy: NotesSortBy,
        searchText: String): Flow<List<NoteEntity>>
    {
        if(searchText.isEmpty()) {
            return when (sortBy) {
                NotesSortBy.Id -> dao.getAllOrderById()
                NotesSortBy.Title -> dao.getAllOrderByTitle()
                NotesSortBy.DateCreatedAsc -> dao.getAllOrderByDateCreated()
                NotesSortBy.DateCreatedDesc -> dao.getAllOrderByDateCreatedDescending()
            }
        }

        return when(sortBy) {
            NotesSortBy.Id -> dao.getFilteredOrderById(searchText)
            NotesSortBy.Title -> dao.getFilteredOrderByTitle(searchText)
            NotesSortBy.DateCreatedAsc -> dao.getFilteredOrderByDateCreated(searchText)
            NotesSortBy.DateCreatedDesc -> dao.getFilteredOrderByDateCreatedDescending(searchText)
        }
    }

    override fun getNoteEntityById(noteId: Long?): NoteEntity? {
        if(noteId == null) {
            return null
        }
        return dao.getOneById(noteId)
    }

    override suspend fun insert(note: NoteEntity): Unit = dao.insert(note)

    override suspend fun update(note: NoteEntity): Unit = dao.update(note)

    override suspend fun delete(note: NoteEntity): Unit = dao.delete(note)

    override suspend fun deleteById(id: Long?): Unit = dao.deleteById(id)
}

interface INotesRepo {
    fun getNoteEntities(
        sortBy: NotesSortBy,
        searchText: String): Flow<List<NoteEntity>>

    fun getNoteEntityById(noteId: Long?): NoteEntity?

    suspend fun insert(note: NoteEntity): Unit

    suspend fun update(note: NoteEntity): Unit

    suspend fun delete(note: NoteEntity): Unit

    suspend fun deleteById(id: Long?): Unit
}
