package com.example.notebook.navigation

sealed class Screen(val route: String) {
    object NotebookList : Screen("notebookList")
    object CreateNotebook : Screen("createNotebook")
}
