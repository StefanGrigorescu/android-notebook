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
    val viewModel = getViewModel<CreateNotebookViewModel>()
    val notebookState by viewModel.notebookState.collectAsState()

    val onSaveNotebook: (CreateNotebookScreenEvent.SaveNotebookEvent) -> Unit = { event ->
        viewModel.onSaveNotebook(event)
    }
    val onEditNotebookTitleEvent: (CreateNotebookScreenEvent.EditNotebookTitleEvent) -> Unit = { event ->
        viewModel.onEditNotebookTitleEvent(event)
    }
    val onEditNotebookDescriptionEvent: (CreateNotebookScreenEvent.EditNotebookDescriptionEvent) -> Unit = { event ->
        viewModel.onEditNotebookDescriptionEvent(event)
    }
    val onEditNotebookPasswordEvent: (CreateNotebookScreenEvent.EditNotebookPasswordEvent) -> Unit = { event ->
        viewModel.onEditNotebookPasswordEvent(event)
    }
    val onEditNotebookConfirmPasswordEvent: (CreateNotebookScreenEvent.EditNotebookConfirmPasswordEvent) -> Unit = { event ->
        viewModel.onEditNotebookConfirmPasswordEvent(event)
    }
    val onClearCreateNotebookFormEvent: (CreateNotebookScreenEvent.ClearCreateNotebookFormEvent) -> Unit = { event ->
        viewModel.onClearCreateNotebookFormEvent(event)
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
                    notebookState.title,
                    onEditNotebookTitleEvent)

                NotebookDescriptionInput(
                    notebookState.description,
                    onEditNotebookDescriptionEvent)

                NotebookPasswordInput(
                    notebookState.password,
                    onEditNotebookPasswordEvent)

                if (notebookState.password.isNotBlank()) {
                    NotebookPasswordConfirmationInput(
                        notebookState.password,
                        notebookState.confirmPassword,
                        onEditNotebookConfirmPasswordEvent)
                }

                // Submit, Clear, and Cancel buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onSaveNotebook(CreateNotebookScreenEvent.SaveNotebookEvent(notebookState.title, notebookState.description, notebookState.password))
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit")
                    }

                    Button(
                        onClick = { onClearCreateNotebookFormEvent(CreateNotebookScreenEvent.ClearCreateNotebookFormEvent) },
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
    onEditNotebookTitleEvent: (CreateNotebookScreenEvent.EditNotebookTitleEvent) -> Unit) {
    OutlinedTextField(
        value = notebookName,
        onValueChange = { newValue: String ->
            onEditNotebookTitleEvent(CreateNotebookScreenEvent.EditNotebookTitleEvent(newValue))
        },
        label = { Text("Notebook Name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookDescriptionInput(
    notebookDescription: String,
    onEditNotebookDescriptionEvent: (CreateNotebookScreenEvent.EditNotebookDescriptionEvent) -> Unit) {
    OutlinedTextField(
        value = notebookDescription,
        onValueChange = { newValue: String ->
            onEditNotebookDescriptionEvent(CreateNotebookScreenEvent.EditNotebookDescriptionEvent(newValue))
        },
        label = { Text("Notebook Description") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookPasswordInput(
    notebookPassword: String,
    onEditNotebookPasswordEvent: (CreateNotebookScreenEvent.EditNotebookPasswordEvent) -> Unit) {
    OutlinedTextField(
        value = notebookPassword,
        onValueChange = { newValue: String ->
            onEditNotebookPasswordEvent(CreateNotebookScreenEvent.EditNotebookPasswordEvent(newValue))
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
    onEditNotebookConfirmPasswordEvent: (CreateNotebookScreenEvent.EditNotebookConfirmPasswordEvent) -> Unit) {
    if (notebookPassword.isNotBlank()) {
        OutlinedTextField(
            value = notebookPasswordConfirm,
            onValueChange = { newValue: String ->
                onEditNotebookConfirmPasswordEvent(CreateNotebookScreenEvent.EditNotebookConfirmPasswordEvent(newValue))
            },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
