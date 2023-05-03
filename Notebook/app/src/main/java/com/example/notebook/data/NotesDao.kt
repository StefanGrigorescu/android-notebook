package com.example.notebook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDao {
    @Query("SELECT * FROM notes WHERE LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY id ASC")
    fun getFilteredOrderById(searchText: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY LOWER(title) ASC")
    fun getFilteredOrderByTitle(searchText: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY date_created ASC")
    fun getFilteredOrderByDateCreated(searchText: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE LOWER(title) LIKE LOWER('%' || :searchText || '%') ORDER BY date_created DESC")
    fun getFilteredOrderByDateCreatedDescending(searchText: String): Flow<List<NoteEntity>>


    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllOrderById(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY LOWER(title) ASC")
    fun getAllOrderByTitle(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY date_created ASC")
    fun getAllOrderByDateCreated(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY date_created DESC")
    fun getAllOrderByDateCreatedDescending(): Flow<List<NoteEntity>>

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
