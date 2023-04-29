package com.example.notebook.notebooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notebook.AppBrand
import com.example.notebook.navigation.BottomBar
import com.example.notebook.navigation.Screen
import org.koin.androidx.compose.getViewModel

@Composable
fun CreateNotebookScreen(
    navController: NavHostController
) {
    val notebooksViewModel = getViewModel<NotebooksViewModel>()
    val notebooksState by notebooksViewModel.notebooksState.collectAsState()

    val onSaveNotebook: (NotebooksEvent.SaveNotebookEvent) -> Unit = { event ->
        notebooksViewModel.onSaveNotebook(event)
    }
    val onEditNotebookTitleEvent: (NotebooksEvent.EditNotebookTitleEvent) -> Unit = { event ->
        notebooksViewModel.onEditNotebookTitleEvent(event)
    }
    val onEditNotebookDescriptionEvent: (NotebooksEvent.EditNotebookDescriptionEvent) -> Unit = { event ->
        notebooksViewModel.onEditNotebookDescriptionEvent(event)
    }
    val onEditNotebookPasswordEvent: (NotebooksEvent.EditNotebookPasswordEvent) -> Unit = { event ->
        notebooksViewModel.onEditNotebookPasswordEvent(event)
    }
    val onEditNotebookConfirmPasswordEvent: (NotebooksEvent.EditNotebookConfirmPasswordEvent) -> Unit = { event ->
        notebooksViewModel.onEditNotebookConfirmPasswordEvent(event)
    }
    val onClearCreateNotebookFormEvent: (NotebooksEvent.ClearCreateNotebookFormEvent) -> Unit = { event ->
        notebooksViewModel.onClearCreateNotebookFormEvent(event)
    }

    Scaffold(
        topBar = {
            AppBrand(Screen.CreateNotebook.screenName)
        },
        bottomBar = { BottomBar(navController = navController) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(PaddingValues(start = 20.dp, end = 20.dp,)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NotebookTitleInput(
                    notebooksState.title,
                    onEditNotebookTitleEvent)

                NotebookDescriptionInput(
                    notebooksState.description,
                    onEditNotebookDescriptionEvent)

                NotebookPasswordInput(
                    notebooksState.password,
                    onEditNotebookPasswordEvent)

                if (notebooksState.password.isNotBlank()) {
                    NotebookPasswordConfirmationInput(
                        notebooksState.password,
                        notebooksState.confirmPassword,
                        onEditNotebookConfirmPasswordEvent)
                }

                // Submit, Clear, and Cancel buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onSaveNotebook(NotebooksEvent.SaveNotebookEvent(notebooksState.title, notebooksState.description, notebooksState.password))
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit")
                    }

                    Button(
                        onClick = { onClearCreateNotebookFormEvent(NotebooksEvent.ClearCreateNotebookFormEvent) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear")
                    }

                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    )
}

@Composable
fun NotebookTitleInput(
    notebookName: String,
    onEditNotebookTitleEvent: (NotebooksEvent.EditNotebookTitleEvent) -> Unit) {
    OutlinedTextField(
        value = notebookName,
        onValueChange = { newValue: String ->
            onEditNotebookTitleEvent(NotebooksEvent.EditNotebookTitleEvent(newValue))
        },
        label = { Text("Notebook Name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookDescriptionInput(
    notebookDescription: String,
    onEditNotebookDescriptionEvent: (NotebooksEvent.EditNotebookDescriptionEvent) -> Unit) {
    OutlinedTextField(
        value = notebookDescription,
        onValueChange = { newValue: String ->
            onEditNotebookDescriptionEvent(NotebooksEvent.EditNotebookDescriptionEvent(newValue))
        },
        label = { Text("Notebook Description") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookPasswordInput(
    notebookPassword: String,
    onEditNotebookPasswordEvent: (NotebooksEvent.EditNotebookPasswordEvent) -> Unit) {
    OutlinedTextField(
        value = notebookPassword,
        onValueChange = { newValue: String ->
            onEditNotebookPasswordEvent(NotebooksEvent.EditNotebookPasswordEvent(newValue))
        },
        label = { Text("Notebook Password (Optional)") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookPasswordConfirmationInput(
    notebookPassword: String, notebookPasswordConfirm: String,
    onEditNotebookConfirmPasswordEvent: (NotebooksEvent.EditNotebookConfirmPasswordEvent) -> Unit) {
    if (notebookPassword.isNotBlank()) {
        OutlinedTextField(
            value = notebookPasswordConfirm,
            onValueChange = { newValue: String ->
                onEditNotebookConfirmPasswordEvent(NotebooksEvent.EditNotebookConfirmPasswordEvent(newValue))
            },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
