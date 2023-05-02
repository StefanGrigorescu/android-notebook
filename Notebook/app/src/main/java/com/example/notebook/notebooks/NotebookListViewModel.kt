package com.example.notebook.notebooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.data.INotebooksRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotebookListViewModel(
    private val repo: INotebooksRepo
): ViewModel() {
    private val _notebooksState = MutableStateFlow(NotebooksState())
    private val _notebooksSortBy = MutableStateFlow(NotebooksSortBy.DateCreatedAsc)
    private val _notebooksSearchText = MutableStateFlow("")
    private val _notebooks = _notebooksSortBy
        .flatMapLatest { sortBy -> repo.getNotebookEntities(sortBy, _notebooksSearchText.value) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val notebooksState = combine(_notebooksState, _notebooksSortBy, _notebooksSearchText, _notebooks) { state, notebooksSortBy, notebooksSearchText, notebooks ->
        state.copy(
            sortBy = notebooksSortBy,
            searchText = notebooksSearchText,
            notebooks = notebooks
                .map { entity -> entity.toNotebook() },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), NotebooksState())

    fun onChangeNotebookPasswordEvent(event: NotebookListScreenEvent.ChangeNotebookPasswordEvent): Unit {

    }

    fun onDeleteNotebook(event: NotebookListScreenEvent.DeleteNotebookEvent): Unit {
        viewModelScope.launch {
            repo.deleteById(event.id)
        }
    }

    fun onSortNotebooks(event: NotebookListScreenEvent.SortNotebooksEvent): Unit {
        _notebooksSortBy.value = event.sortBy
    }

    fun onSearchNotebooks(event: NotebookListScreenEvent.SearchNotebookEvent): Unit {
        _notebooksSearchText.value = event.searchText
    }
}

sealed interface NotebookListScreenEvent {
    data class ChangeNotebookPasswordEvent(val newPassword: String, val confirmNewPassword: String): NotebookListScreenEvent
    data class DeleteNotebookEvent(val id: Long?): NotebookListScreenEvent

    data class SortNotebooksEvent(val sortBy: NotebooksSortBy): NotebookListScreenEvent
    data class SearchNotebookEvent(val searchText: String): NotebookListScreenEvent
}

enum class NotebooksSortBy {
    Id,
    Title,
    DateCreatedAsc,
    DateCreatedDesc;

    override fun toString(): String {
        return when (this) {
            Id -> "ID"
            Title -> "Title"
            DateCreatedAsc -> "Date created (asc)"
            DateCreatedDesc -> "Date created (desc)"
        }
    }
}

data class NotebooksState(
    val notebooks: List<Notebook> = emptyList(),
    val sortBy: NotebooksSortBy = NotebooksSortBy.DateCreatedAsc,
    val searchText: String = "",

    var inputCurrentPassword: String = ""
)
