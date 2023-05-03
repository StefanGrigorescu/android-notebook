package com.example.notebook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE notebook=:notebookId AND LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY id ASC")
    fun getFilteredOrderById(notebookId: Long?, searchText: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE notebook=:notebookId AND LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY LOWER(title) ASC")
    fun getFilteredOrderByTitle(notebookId: Long?, searchText: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE notebook=:notebookId AND LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY date_created ASC")
    fun getFilteredOrderByDateCreated(notebookId: Long?, searchText: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE notebook=:notebookId AND LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY date_created DESC")
    fun getFilteredOrderByDateCreatedDescending(notebookId: Long?, searchText: String): Flow<List<NoteEntity>>


    @Query("SELECT * FROM notes WHERE notebook=:notebookId ORDER BY id ASC")
    fun getAllByNotebookIdOrderById(notebookId: Long?): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE notebook=:notebookId ORDER BY LOWER(title) ASC")
    fun getAllByNotebookIdOrderByTitle(notebookId: Long?): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE notebook=:notebookId ORDER BY date_created ASC")
    fun getAllByNotebookIdOrderByDateCreated(notebookId: Long?): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE notebook=:notebookId ORDER BY date_created DESC")
    fun getAllByNotebookIdOrderByDateCreatedDescending(notebookId: Long?): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    fun getOneById(id: Long?): NoteEntity?

    @Insert
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long?)
}
