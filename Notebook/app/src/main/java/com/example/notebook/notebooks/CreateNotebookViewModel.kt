package com.example.notebook.notebooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.data.INotebooksRepo
import com.example.notebook.data.NotebookEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CreateNotebookViewModel(
    private val repo: INotebooksRepo
): ViewModel() {
    private val _notebookState = MutableStateFlow(NotebookState())
    val notebookState = _notebookState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), NotebookState())

    fun onSaveNotebook(event: CreateNotebookScreenEvent.SaveNotebookEvent): Unit {
        val title = notebookState.value.title
        val description = notebookState.value.description
        val password = notebookState.value.password
        val passwordConfirm = notebookState.value.confirmPassword

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
            repo.insert(notebookEntity)
        }

        _notebookState.update { it.copy(
            title = "",
            description = "",
            password = "",
            confirmPassword = "",
            inputCurrentPassword = ""
        ) }
    }

    fun onEditNotebookTitleEvent(event: CreateNotebookScreenEvent.EditNotebookTitleEvent): Unit {
        _notebookState.update { it.copy(
            title = event.title
        )  }
    }

    fun onEditNotebookDescriptionEvent(event: CreateNotebookScreenEvent.EditNotebookDescriptionEvent): Unit {
        _notebookState.update { it.copy(
            description = event.description
        )  }
    }

    fun onEditNotebookPasswordEvent(event: CreateNotebookScreenEvent.EditNotebookPasswordEvent): Unit {
        _notebookState.update { it.copy(
            password = event.password
        )  }

        if(event.password.isEmpty()) {
            _notebookState.update { it.copy(
                confirmPassword = ""
            ) }
        }
    }

    fun onEditNotebookConfirmPasswordEvent(event: CreateNotebookScreenEvent.EditNotebookConfirmPasswordEvent): Unit {
        _notebookState.update { it.copy(
            confirmPassword = event.confirmPassword
        )  }
    }

    fun onClearCreateNotebookFormEvent(event: CreateNotebookScreenEvent.ClearCreateNotebookFormEvent): Unit {
        _notebookState.update { it.copy(
            title = "",
            description = "",
            password = "",
            confirmPassword = ""
        )  }
    }
}

sealed interface CreateNotebookScreenEvent {
    data class SaveNotebookEvent(val title: String, val description: String, val password: String?): NotebookListScreenEvent
    data class EditNotebookTitleEvent(val title: String): NotebookListScreenEvent
    data class EditNotebookDescriptionEvent(val description: String): NotebookListScreenEvent
    data class EditNotebookPasswordEvent(val password: String): NotebookListScreenEvent
    data class EditNotebookConfirmPasswordEvent(val confirmPassword: String): NotebookListScreenEvent
    object ClearCreateNotebookFormEvent: NotebookListScreenEvent

}

data class NotebookState(
    var title: String = "",
    var description: String = "",
    var password: String = "",
    var confirmPassword: String = "",

    var inputCurrentPassword: String = ""
)
