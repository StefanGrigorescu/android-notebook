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
    private val _notebooksState = MutableStateFlow(NotebooksState())
    private val _notebooksSortBy = MutableStateFlow(NotebooksSortBy.DateCreatedAsc)
    private val _notebooksSearchText = MutableStateFlow("")
    private val _notebooks = _notebooksSortBy
        .flatMapLatest { sortBy -> getNotebookEntities(sortBy, _notebooksSearchText.value, dao) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val notebooksState = combine(_notebooksState, _notebooksSortBy, _notebooksSearchText, _notebooks) { state, notebooksSortBy, notebooksSearchText, notebooks ->
        state.copy(
            sortBy = notebooksSortBy,
            searchText = notebooksSearchText,
            notebooks = notebooks
                .map { entity -> entity.toNotebook() },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), NotebooksState())

    fun onSaveNotebook(event: NotebooksEvent.SaveNotebookEvent) {
        val title = notebooksState.value.title
        val description = notebooksState.value.description
        val password = notebooksState.value.password
        val passwordConfirm = notebooksState.value.confirmPassword

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
            confirmPassword = "",
            inputCurrentPassword = ""
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

    fun onEditNotebookPasswordEvent(event: NotebooksEvent.EditNotebookPasswordEvent) {
        _notebooksState.update { it.copy(
            password = event.password
        )  }

        if(event.password.isEmpty()) {
            _notebooksState.update { it.copy(
                confirmPassword = ""
            ) }
        }
    }

    fun onEditNotebookConfirmPasswordEvent(event: NotebooksEvent.EditNotebookConfirmPasswordEvent) {
        _notebooksState.update { it.copy(
            confirmPassword = event.confirmPassword
        )  }
    }

    fun onChangeNotebookPasswordEvent(event: NotebooksEvent.ChangeNotebookPasswordEvent) {

    }

    fun onClearCreateNotebookFormEvent(event: NotebooksEvent.ClearCreateNotebookFormEvent) {
        _notebooksState.update { it.copy(
            title = "",
            description = "",
            password = "",
            confirmPassword = ""
        )  }
    }

    fun onDeleteNotebook(event: NotebooksEvent.DeleteNotebookEvent) {
        viewModelScope.launch {
            dao.deleteById(event.id)
        }
    }

    fun onSortNotebooks(event: NotebooksEvent.SortNotebooksEvent) {
        _notebooksSortBy.value = event.sortBy
    }

    fun onSearchNotebooks(event: NotebooksEvent.SearchNotebookEvent) {
        _notebooksSearchText.value = event.searchText
    }
}

sealed interface NotebooksEvent {
    data class SaveNotebookEvent(val title: String, val description: String, val password: String?): NotebooksEvent
    data class EditNotebookTitleEvent(val title: String): NotebooksEvent
    data class EditNotebookDescriptionEvent(val description: String): NotebooksEvent
    data class EditNotebookPasswordEvent(val password: String): NotebooksEvent
    data class EditNotebookConfirmPasswordEvent(val confirmPassword: String): NotebooksEvent
    data class ChangeNotebookPasswordEvent(val newPassword: String, val confirmNewPassword: String): NotebooksEvent
    object ClearCreateNotebookFormEvent: NotebooksEvent

    data class DeleteNotebookEvent(val id: Long?): NotebooksEvent
    data class SortNotebooksEvent(val sortBy: NotebooksSortBy): NotebooksEvent
    data class SearchNotebookEvent(val searchText: String): NotebooksEvent
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
    val searchText: String = "",

    var title: String = "",
    var description: String = "",
    var password: String = "",
    var confirmPassword: String = "",

    var inputCurrentPassword: String = ""
)

private fun getNotebookEntities(
    sortBy: NotebooksSortBy,
    searchText: String,
    dao: NotebooksDao)
: Flow<List<NotebookEntity>> {
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
