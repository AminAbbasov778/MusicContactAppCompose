package com.example.musiccontactapp.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.musiccontactapp.presentation.screens.ChoiceScreen
import com.example.musiccontactapp.presentation.screens.ContactScreen
import com.example.musiccontactapp.presentation.screens.MusicsScreen
import com.example.musiccontactapp.presentation.screens.PlayScreen
import com.example.musiccontactapp.presentation.viewmodels.MusicViewModel


@Composable
fun Navigation(innerPadding: PaddingValues, username: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "choice") {
        composable("choice") {
            ChoiceScreen(
                username = username,
                onMusicsClick = { navController.navigate("music_graph") },
                onContactsClick = { navController.navigate("contacts") },
                paddingValues = innerPadding
            )
        }

        navigation(startDestination = "music", route = "music_graph") {

            composable("music") {
                val parentEntry = remember(it) { navController.getBackStackEntry("music_graph") }
                val viewModel: MusicViewModel = hiltViewModel(parentEntry)
                MusicsScreen(viewModel = viewModel, onMusicsClick = {

                    navController.navigate("playmusic")
                })
            }

            composable(
                route = "playmusic",

            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("music_graph")
                }
                val viewModel: MusicViewModel = hiltViewModel(parentEntry)

                PlayScreen(
                    viewModel = viewModel, onBackClick = { navController.navigate("music") }
                )
            }
        }

        composable("contacts") {

            ContactScreen(viewModel = hiltViewModel())
        }
    }

}






