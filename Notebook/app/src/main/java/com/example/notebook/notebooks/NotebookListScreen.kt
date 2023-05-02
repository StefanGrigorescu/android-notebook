package com.example.notebook.notebooks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notebook.AppBrand
import com.example.notebook.navigation.BottomBar
import com.example.notebook.navigation.Screen
import org.koin.androidx.compose.getViewModel

@Composable
fun NotebookListScreen(
    navController: NavHostController
) {
    val notebooksViewModel: NotebookListViewModel = getViewModel<NotebookListViewModel>()
    val notebooksState: NotebooksState by notebooksViewModel.notebooksState.collectAsState()

    val onSortNotebooks: (NotebookListScreenEvent.SortNotebooksEvent) -> Unit = { event ->
        notebooksViewModel.onSortNotebooks(event)
    }
    val onSearchNotebooks: (NotebookListScreenEvent.SearchNotebookEvent) -> Unit = { event ->
        notebooksViewModel.onSearchNotebooks(event)
    }

    Scaffold(
        topBar = {
            AppBrand(Screen.NotebookList.screenName)
        },
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = { AddNotebookButton(navController) },
        floatingActionButtonPosition = FabPosition.End,
        content = {padding ->
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    NotebookSearchView(notebooksState.searchText, onSearchNotebooks = onSearchNotebooks)
                }
                item {
                    NotebookSortView(notebooksState, onSortNotebooks)
                }
                if (notebooksState.notebooks.isEmpty()) {
                    val noNotebooksYet = "There is no notebook created yet. Try creating one!"

                    item {
                        Text(
                            text = noNotebooksYet,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                } else {
                    items(items = notebooksState.notebooks) { notebook ->
                        NotebookListItem(notebook)
                    }
                }
            }
        }
    )
}

@Composable
fun AddNotebookButton(navController: NavHostController) {
    val addNotebookButtonDescription = "Add a new notebook"

    FloatingActionButton(
        onClick = {
            navController.navigate(Screen.CreateNotebook.route)
        },
        content = { Icon(Icons.Filled.Add, contentDescription = addNotebookButtonDescription) },
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
    )
}

@Composable
fun NotebookSearchView(
    stateSearchText: String,
    onSearchNotebooks: (NotebookListScreenEvent.SearchNotebookEvent) -> Unit
) {
    val searchNotebookHint = "Search notebook by name or by description"

    TextField(
        value = stateSearchText,
        onValueChange = { newSearchText: String ->
            onSearchNotebooks(NotebookListScreenEvent.SearchNotebookEvent(newSearchText))
        },
        label = { Text(searchNotebookHint) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookSortView(
    notebooksState: NotebooksState,
    onSortNotebooks: (NotebookListScreenEvent.SortNotebooksEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = CenterVertically
    ) {
        NotebooksSortBy.values().forEach { sortBy ->
            Row(
                modifier = Modifier
                    .clickable {
                        onSortNotebooks(NotebookListScreenEvent.SortNotebooksEvent(sortBy))
                    },
                verticalAlignment = CenterVertically
            ) {
                RadioButton(
                    selected = notebooksState.sortBy == sortBy,
                    onClick = {
                        onSortNotebooks(NotebookListScreenEvent.SortNotebooksEvent(sortBy))
                    })
                Text(text = sortBy.toString())
            }
        }
    }
}

@Composable
fun NotebookListItem(notebook: Notebook) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(PaddingValues(start = 20.dp, ))
                .weight(1f)
                .clickable(onClick = {
                    /* Handle notebook item click */
                }),
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
        IconButton(
            onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
        }
    }
}
