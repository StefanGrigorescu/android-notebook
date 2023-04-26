package com.example.notebook.notebooks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.example.notebook.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notebook.AppBrand
import com.example.notebook.models.Notebook
import com.example.notebook.navigation.Screen
import java.util.*

@Composable
fun NotebookList(navController: NavController) {
    val notebooks: List<Notebook> = getNotebooks()
    val (filteredNotebooks, setFilteredNotebooks) = remember { mutableStateOf(notebooks) }

    Scaffold(
        topBar = {
            AppBrand("Notebooks List")
        },
        floatingActionButton = { AddNotebookButton(navController) },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                NotebookSearchView(notebooks, onSearch = setFilteredNotebooks)
                NotebookListView(filteredNotebooks, it)
            }
        }
    )
}

private fun getNotebooks(): List<Notebook> {
    // TODO: Implement a method to retrieve the list of notebooks
    // from a data source such as a database or a web service.

    val notebooks = mutableListOf<Notebook>()

    for (i in 1..2500) {
        val notebook = Notebook()
        notebook.title = "Notebook $i"
        notebook.description = "This is notebook number $i"
        notebooks.add(notebook)
    }

    return notebooks

    // return emptyList()
}

@Composable
fun NotebookSearchView(
    notebooks: List<Notebook>,
    onSearch: (List<Notebook>) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = { newSearchText ->
            searchText = newSearchText
            onSearch(getFilteredNotebooks(notebooks, newSearchText))
        },
        label = { Text(stringResource(id = R.string.search_notebooks_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}

private fun getFilteredNotebooks(
    notebooks: List<Notebook>,
    searchText: String
): List<Notebook> {
    if (searchText.isBlank()) {
        return notebooks
    }

    val searchTerm = searchText.lowercase(Locale.getDefault())

    return notebooks.filter { notebook ->
        notebook.title.lowercase(Locale.getDefault()).contains(searchTerm) ||
                notebook.description.lowercase(Locale.getDefault()).contains(searchTerm)
    }
}

@Composable
fun NotebookListView(notebooks: List<Notebook>, padding: PaddingValues) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (notebooks.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_notebooks),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = padding
            ) {
                itemsIndexed(notebooks) { _, notebook ->
                    NotebookListItem(notebook)
                }
            }
        }
    }
}

@Composable
fun NotebookListItem(notebook: Notebook) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(onClick = { /* Handle notebook item click */ }),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = notebook.title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = notebook.description,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun AddNotebookButton(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(Screen.CreateNotebook.route) },
        content = { Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.add_notebook_button_description)) },
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
    )
}
