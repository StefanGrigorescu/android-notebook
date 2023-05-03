package com.example.notebook.notes

sealed interface NotesEvent {
    // NoteListScreen Events:
    data class DeleteNoteEvent(val id: Long?): NotesEvent

    data class SortNotesEvent(val sortBy: NotesSortBy): NotesEvent
    data class SearchNoteEvent(val searchText: String): NotesEvent

}
