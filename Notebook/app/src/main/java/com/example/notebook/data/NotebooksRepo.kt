package com.example.notebook.data

import com.example.notebook.api.SampleAPI
import com.example.notebook.notebooks.NotebooksSortBy
import kotlinx.coroutines.flow.Flow

class NotebooksRepo(
    private val dao: NotebooksDao,
    private val api: SampleAPI
): INotebooksRepo {
    override fun getNotebookEntities(
        sortBy: NotebooksSortBy,
        searchText: String): Flow<List<NotebookEntity>>
    {
        if(searchText.isEmpty()) {
            return when (sortBy) {
                NotebooksSortBy.Id -> dao.getAllOrderById()
                NotebooksSortBy.Title -> dao.getAllOrderByTitle()
                NotebooksSortBy.DateCreatedAsc -> dao.getAllOrderByDateCreated()
                NotebooksSortBy.DateCreatedDesc -> dao.getAllOrderByDateCreatedDescending()
            }
        }

        return when(sortBy) {
            NotebooksSortBy.Id -> dao.getFilteredOrderById(searchText)
            NotebooksSortBy.Title -> dao.getFilteredOrderByTitle(searchText)
            NotebooksSortBy.DateCreatedAsc -> dao.getFilteredOrderByDateCreated(searchText)
            NotebooksSortBy.DateCreatedDesc -> dao.getFilteredOrderByDateCreatedDescending(searchText)
        }
    }

    override fun getNotebookEntityById(notebookId: Long?): NotebookEntity? {
        if(notebookId == null) {
            return null
        }
        return dao.getOneById(notebookId)
    }

    override fun checkPassword(
        notebookId: Long?,
        notebookPasswordInput: String): Boolean
    {
        if(notebookId == null) {
            return false;
        }

        val notebookPassword: String = dao.getOneById(notebookId)?.password ?: return false
        return notebookPasswordInput == notebookPassword;
    }

    override suspend fun insert(notebook: NotebookEntity): Unit = dao.insert(notebook)

    override suspend fun update(notebook: NotebookEntity): Unit = dao.update(notebook)

    override suspend fun delete(notebook: NotebookEntity): Unit = dao.delete(notebook)

    override suspend fun deleteById(id: Long?): Unit = dao.deleteById(id)

    override fun getSampleFromApi(): String {
        return api.getSample()
    }
}

interface INotebooksRepo {
    fun getNotebookEntities(
        sortBy: NotebooksSortBy,
        searchText: String): Flow<List<NotebookEntity>>

    fun getNotebookEntityById(notebookId: Long?): NotebookEntity?

    fun checkPassword(notebookId: Long?, notebookPasswordInput: String): Boolean

    suspend fun insert(notebook: NotebookEntity): Unit

    suspend fun update(notebook: NotebookEntity): Unit

    suspend fun delete(notebook: NotebookEntity): Unit

    suspend fun deleteById(id: Long?): Unit

    fun getSampleFromApi(): String
}
