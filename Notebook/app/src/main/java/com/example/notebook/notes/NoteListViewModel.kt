package com.example.notebook.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.data.INotesRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NoteListViewModel(
    private val repo: INotesRepo
): ViewModel() {
    private val _screenState = MutableStateFlow(NoteListScreenState())
    private val _sortByState = MutableStateFlow(NotesSortBy.DateCreatedAsc)
    private val _searchTextState = MutableStateFlow("")
    private val _notesState = _sortByState
        .flatMapLatest { sortBy -> repo.getNoteEntities(sortBy, _searchTextState.value) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val screenState = combine(_screenState, _sortByState, _searchTextState, _notesState) { state, sortBy, searchText, notes ->
        state.copy(
            sortBy = sortBy,
            searchText = searchText,
            notes = notes
                .map { entity -> entity.toNote() },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), NoteListScreenState())

    fun onEvent(event: NotesEvent): Unit {
        when(event) {
            is NotesEvent.SearchNoteEvent -> {
                _searchTextState.value = event.searchText
            }
            is NotesEvent.SortNotesEvent -> {
                _sortByState.value = event.sortBy
            }
            is NotesEvent.DeleteNoteEvent -> {
                viewModelScope.launch {
                    repo.deleteById(event.id)
                }
            }
            else -> {
                // Do nothing for other events
            }
        }
    }
}

enum class NotesSortBy {
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

data class NoteListScreenState(
    val notes: List<Note> = emptyList(),
    val sortBy: NotesSortBy = NotesSortBy.DateCreatedAsc,
    val searchText: String = "",
)
