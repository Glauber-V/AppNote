package com.example.appnote

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appnote.ui.NotesViewModel
import com.example.appnote.ui.theme.AppNoteTheme

@Composable
fun AppNoteApp() {
    val navHostController: NavHostController = rememberNavController()
    val notesViewModel: NotesViewModel = viewModel()

    AppNoteTheme {
        AppNoteNavHost(
            navController = navHostController,
            startDestination = AllNotesDestination.route,
            notesViewModel = notesViewModel
        )
    }
}