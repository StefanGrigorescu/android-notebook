package com.example.notebook.notebooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notebook.AppBrand
import com.example.notebook.navigation.BottomBar
import com.example.notebook.navigation.Screen

@Composable
fun CreateNotebook(navController: NavHostController) {
    var notebookName by rememberSaveable("notebookName") { mutableStateOf("") }
    var notebookDescription by rememberSaveable("notebookDescription") { mutableStateOf("") }
    var notebookPassword by rememberSaveable("notebookPassword") { mutableStateOf("") }
    var notebookPasswordConfirm by rememberSaveable("notebookPasswordConfirm") { mutableStateOf("") }

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
                    .padding(PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                    )),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NotebookNameInput(notebookName, onNotebookNameChange = { notebookName = it })
                NotebookDescriptionInput(
                    notebookDescription,
                    onNotebookDescriptionChange = { notebookDescription = it })
                NotebookPasswordInput(
                    notebookPassword,
                    onNotebookPasswordChange = { notebookPassword = it })
                if (notebookPassword.isNotBlank()) {
                    NotebookPasswordConfirmationInput(
                        notebookPassword,
                        notebookPasswordConfirm,
                        onNotebookPasswordConfirmChange = { notebookPasswordConfirm = it })
                }

                // Submit, Clear, and Cancel buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            // TODO: Save notebook to database
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit")
                    }

                    Button(
                        onClick = {
                            notebookName = ""
                            notebookDescription = ""
                            notebookPassword = ""
                            notebookPasswordConfirm = ""
                        },
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
fun NotebookNameInput(notebookName: String, onNotebookNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = notebookName,
        onValueChange = onNotebookNameChange,
        label = { Text("Notebook Name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookDescriptionInput(notebookDescription: String, onNotebookDescriptionChange: (String) -> Unit) {
    OutlinedTextField(
        value = notebookDescription,
        onValueChange = onNotebookDescriptionChange,
        label = { Text("Notebook Description") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookPasswordInput(notebookPassword: String, onNotebookPasswordChange: (String) -> Unit) {
    OutlinedTextField(
        value = notebookPassword,
        onValueChange = onNotebookPasswordChange,
        label = { Text("Notebook Password (Optional)") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NotebookPasswordConfirmationInput(notebookPassword: String, notebookPasswordConfirm: String, onNotebookPasswordConfirmChange: (String) -> Unit) {
    if (notebookPassword.isNotBlank()) {
        OutlinedTextField(
            value = notebookPasswordConfirm,
            onValueChange = onNotebookPasswordConfirmChange,
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
