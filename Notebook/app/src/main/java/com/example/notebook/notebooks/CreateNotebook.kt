package com.example.notebook.notebooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notebook.ui.theme.NotebookTheme

@Composable
fun CreateNotebook(navController: NavController) {
    var notebookName by rememberSaveable("notebookName") { mutableStateOf("") }
    var notebookDescription by rememberSaveable("notebookDescription") { mutableStateOf("") }
    var notebookPassword by rememberSaveable("notebookPassword") { mutableStateOf("") }
    var notebookPasswordConfirm by rememberSaveable("notebookPasswordConfirm") { mutableStateOf("") }

    NotebookTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notebook name input
            OutlinedTextField(
                value = notebookName,
                onValueChange = { notebookName = it },
                label = { Text("Notebook Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Notebook description input
            OutlinedTextField(
                value = notebookDescription,
                onValueChange = { notebookDescription = it },
                label = { Text("Notebook Description") },
                modifier = Modifier.fillMaxWidth()
            )

            // Notebook password input
            OutlinedTextField(
                value = notebookPassword,
                onValueChange = { notebookPassword = it },
                label = { Text("Notebook Password (Optional)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Notebook password confirmation input
            if (notebookPassword.isNotBlank()) {
                OutlinedTextField(
                    value = notebookPasswordConfirm,
                    onValueChange = { notebookPasswordConfirm = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
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
}
