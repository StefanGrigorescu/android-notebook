package com.example.notebook.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notebook.AppBrand
import com.example.notebook.navigation.BottomBar

@Composable
fun ProfileDetails(navController: NavHostController) {
    Scaffold(
        topBar = {
            AppBrand("Profile")
        },
        bottomBar = { BottomBar(navController = navController) },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
//            contentAlignment = Alignment.Center
            ) {
                var crtUser = getCurrentUser()

                if(crtUser.isEmpty()) {
                    crtUser = "User is not authenticated"
                }

                Text(
                    text = crtUser,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    modifier = Modifier.padding(PaddingValues(start = 20.dp, top=8.dp))
                )
            }
        }
    )
}

private fun getCurrentUser(): String {
    // TODO: Implement auth logic (with google I think)
    return ""
}
