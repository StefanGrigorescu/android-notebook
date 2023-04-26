package com.example.notebook.notebooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.data.NotebookEntity
import com.example.notebook.data.NotebooksDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotebooksViewModel(
    private val dao: NotebooksDao
): ViewModel() {
    private val _notebooksSortBy = MutableStateFlow(NotebooksSortBy.DateCreatedAsc)
    private val _notebooks = _notebooksSortBy
        .flatMapLatest { sortType ->
            when(sortType) {
                NotebooksSortBy.Id -> dao.getAllOrderById()
                NotebooksSortBy.Title -> dao.getAllOrderByTitle()
                NotebooksSortBy.DateCreatedAsc -> dao.getAllOrderByDateCreated()
                NotebooksSortBy.DateCreatedDesc -> dao.getAllOrderByDateCreatedDescending()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _notebooksState = MutableStateFlow(NotebooksState())
    val notebooksState = combine(_notebooksState, _notebooksSortBy, _notebooks) { state, notebooksSortBy, notebooks ->
        state.copy(
            sortBy = notebooksSortBy,
            notebooks = notebooks
                .map { entity -> entity.toNotebook() },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), NotebooksState())

    fun onSaveNotebook(event: NotebooksEvent.SaveNotebookEvent) {
        val title = notebooksState.value.title
        val description = notebooksState.value.description
        val password = notebooksState.value.password
        val passwordConfirm = notebooksState.value.passwordConfirm

        if(title.isEmpty() || description.isEmpty()) {
            return
        }
        if(password.isEmpty() != passwordConfirm.isEmpty()) {
            return
        }
        if(password.isNotEmpty() && password != passwordConfirm) {
            return
        }

        val notebookEntity = NotebookEntity(
            title = title,
            description = description,
            password = password
        )

        viewModelScope.launch {
            dao.insert(notebookEntity)
        }

        _notebooksState.update { it.copy(
            title = "",
            description = "",
            password = "",
            passwordConfirm = ""
        ) }
    }

    fun onEditNotebookTitleEvent(event: NotebooksEvent.EditNotebookTitleEvent) {
        _notebooksState.update { it.copy(
            title = event.title
        )  }
    }

    fun onEditNotebookDescriptionEvent(event: NotebooksEvent.EditNotebookDescriptionEvent) {
        _notebooksState.update { it.copy(
            description = event.description
        )  }
    }

    fun onChangeNotebookPasswordEvent(event: NotebooksEvent.ChangeNotebookPasswordEvent) {

    }

    fun onDeleteNotebook(event: NotebooksEvent.DeleteNotebookEvent) {
        viewModelScope.launch {
            dao.deleteById(event.id)
        }
    }

    fun onSortNotebooks(event: NotebooksEvent.SortNotebooksEvent) {
        _notebooksSortBy.value = event.sortBy
    }
}

sealed interface NotebooksEvent {
    data class SaveNotebookEvent(val title: String, val description: String, val password: String?): NotebooksEvent
    data class EditNotebookTitleEvent(val title: String): NotebooksEvent
    data class EditNotebookDescriptionEvent(val description: String): NotebooksEvent
    data class ChangeNotebookPasswordEvent(val newPassword: String, val confirmNewPassword: String): NotebooksEvent
    data class DeleteNotebookEvent(val id: Long?): NotebooksEvent
    data class SortNotebooksEvent(val sortBy: NotebooksSortBy): NotebooksEvent
}

enum class NotebooksSortBy {
    Id,
    Title,
    DateCreatedAsc,
    DateCreatedDesc
}

data class NotebooksState(
    val notebooks: List<Notebook> = emptyList(),
    val sortBy: NotebooksSortBy = NotebooksSortBy.DateCreatedAsc,

    var title: String = "",
    var description: String = "",
    var password: String = "",
    var passwordConfirm: String = ""
)
