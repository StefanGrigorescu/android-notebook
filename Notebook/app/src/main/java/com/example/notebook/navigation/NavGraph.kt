package com.example.notebook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notebook.notebooks.CreateNotebookScreen
import com.example.notebook.notebooks.NotebookListScreen
import com.example.notebook.notebooks.NotebookLockScreen
import com.example.notebook.profile.ProfileDetails
import com.example.notebook.settings.Settings

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NotebookList.route
    ) {
        composable(Screen.NotebookList.route) { NotebookListScreen(navController = navController) }
        composable(Screen.CreateNotebook.route) { CreateNotebookScreen(navController = navController) }
        composable(Screen.NotebookLock.route) { backStackEntry ->
            val notebookId: Long? = backStackEntry.arguments?.getString("notebookId")?.toLongOrNull()
            requireNotNull(notebookId) { "NavigationError: notebookId parameter not found." }
            NotebookLockScreen(navController = navController, notebookId = notebookId)
        }
        composable(BottomBarScreen.Profile.route) { ProfileDetails(navController = navController) }
        composable(BottomBarScreen.Settings.route) { Settings(navController = navController) }
    }
}
