package com.example.notebook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllOrderById(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY LOWER(title) ASC")
    fun getAllOrderByTitle(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY date_created ASC")
    fun getAllOrderByDateCreated(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY date_created DESC")
    fun getAllOrderByDateCreatedDescending(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insert(notebook: NoteEntity)

    @Update
    suspend fun update(notebook: NoteEntity)

    @Delete
    suspend fun delete(notebook: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long?)
}
