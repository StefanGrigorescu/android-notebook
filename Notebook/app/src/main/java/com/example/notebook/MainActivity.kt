package com.example.notebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notebook.models.Notebook
import com.example.notebook.ui.theme.NotebookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotebookTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NotebookList(getNotebooks())
                }
            }
        }
    }

    private fun getNotebooks(): List<Notebook> {
        // TODO: Implement a method to retrieve the list of notebooks
        // from a data source such as a database or a web service.
        return emptyList()
    }
}

@Composable
fun NotebookList(notebooks: List<Notebook>) {
    val notebooks by remember { mutableStateOf(notebooks) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(id = R.string.app_name)) }) },
        floatingActionButton = { AddNotebookButton() },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            NotebookListView(notebooks, it)
        }
    )
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
                style = MaterialTheme.typography.h6,
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
fun AddNotebookButton() {
    FloatingActionButton(
        onClick = { /* TODO */ },
        content = { Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.add_notebook_button_description)) },
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
    )
}
