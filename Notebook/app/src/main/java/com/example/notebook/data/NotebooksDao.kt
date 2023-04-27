package com.example.notebook.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotebooksDao {
    @Query("SELECT * FROM notebooks WHERE (LOWER(title) LIKE LOWER('%:searchText%') OR LOWER(description) LIKE LOWER('%:searchText%')) ORDER BY id ASC")
    fun getFilteredOrderById(searchText: String): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks WHERE (LOWER(title) LIKE LOWER('%:searchText%') OR LOWER(description) LIKE LOWER('%:searchText%')) ORDER BY title ASC")
    fun getFilteredOrderByTitle(searchText: String): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks WHERE (LOWER(title) LIKE LOWER('%:searchText%') OR LOWER(description) LIKE LOWER('%:searchText%')) ORDER BY date_created ASC")
    fun getFilteredOrderByDateCreated(searchText: String): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks WHERE (LOWER(title) LIKE LOWER('%:searchText%') OR LOWER(description) LIKE LOWER('%:searchText%')) ORDER BY date_created DESC")
    fun getFilteredOrderByDateCreatedDescending(searchText: String): Flow<List<NotebookEntity>>


    @Query("SELECT * FROM notebooks ORDER BY id ASC")
    fun getAllOrderById(): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks ORDER BY title ASC")
    fun getAllOrderByTitle(): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks ORDER BY date_created ASC")
    fun getAllOrderByDateCreated(): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks ORDER BY date_created DESC")
    fun getAllOrderByDateCreatedDescending(): Flow<List<NotebookEntity>>


    @Insert
    suspend fun insert(notebook: NotebookEntity)

    @Update
    suspend fun update(notebook: NotebookEntity)

    @Delete
    suspend fun delete(notebook: NotebookEntity)

    @Query("DELETE FROM notebooks WHERE id = :id")
    suspend fun deleteById(id: Long?)
}
