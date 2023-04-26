package com.example.notebook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notebook.notebooks.CreateNotebook
import com.example.notebook.notebooks.NotebookList
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
        composable(Screen.NotebookList.route) { NotebookList(navController = navController) }
        composable(Screen.CreateNotebook.route) { CreateNotebook(navController = navController) }
        composable(BottomBarScreen.Profile.route) { ProfileDetails(navController = navController) }
        composable(BottomBarScreen.Settings.route) { Settings(navController = navController) }
    }
}
