package com.example.notebook.notebooks

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notebook.AppBrand
import com.example.notebook.navigation.BottomBar
import com.example.notebook.navigation.Screen
import org.koin.androidx.compose.getViewModel

@Composable
fun NotebookLockScreen(
    notebookId: Long?,
    navController: NavHostController
) {
    val viewModel: NotebookLockViewModel = getViewModel<NotebookLockViewModel>()
    val state: NotebookLockScreenState by viewModel.screenState.collectAsState()

    Scaffold(
        topBar = {
            AppBrand(Screen.NotebookLock.screenName)
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(PaddingValues(start = 20.dp, end = 20.dp,)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NotebookPasswordInput(
                state.password
            ) { event -> viewModel.onEvent(event) }
        }
    }
}
