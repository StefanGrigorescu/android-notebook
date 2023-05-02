package com.example.notebook.notebooks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.data.INotebooksRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotebookLockViewModel(
    savedStateHandle: SavedStateHandle,
    private val repo: INotebooksRepo,
): ViewModel() {
    private val _screenState = MutableStateFlow(NotebookLockScreenState())
    val screenState = _screenState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        NotebookLockScreenState()
    )

    private val notebookId: Long = checkNotNull(savedStateHandle["notebookId"])
    var notebookTitle: String? = repo.getNotebookEntityById(notebookId)?.title

    fun onEvent(event: NotebooksEvent): Unit {
        when(event) {
            is NotebooksEvent.SubmitFormEvent -> {
                viewModelScope.launch {
                    val isPasswordCorrect = repo.checkPassword(notebookId, screenState.value.password)

                    _screenState.update { it.copy(
                        isPasswordCorrect = isPasswordCorrect,
                        isSubmitted = true,
                    ) }
                }
            }
            NotebooksEvent.ClearFormEvent -> {
                _screenState.update { it.copy(
                    password = "",
                    isPasswordCorrect = false,
                    isSubmitted = false,
                ) }
            }
            is NotebooksEvent.SetPasswordEvent -> {
                _screenState.update { it.copy(
                    password = event.password
                )  }
            }
            else -> {
                // Do nothing for other events
            }
        }
    }
}

data class NotebookLockScreenState(
    var password: String = "",
    var isPasswordCorrect: Boolean = false,
    var isSubmitted: Boolean = false,
)
