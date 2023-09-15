package com.example.appnote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appnote.data.model.Note
import com.example.appnote.ui.AddNoteScreen
import com.example.appnote.ui.AllNotesScreen
import com.example.appnote.ui.EditNoteScreen
import com.example.appnote.ui.NotesViewModel

@Composable
fun AppNoteNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    notesViewModel: NotesViewModel,
    navActions: AppNoteNavigationActions = remember(navController) {
        AppNoteNavigationActions(navController)
    }
) {
    val notes by notesViewModel.notes.observeAsState(initial = emptyList())

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppNoteDestinations.ALL_NOTES_ROUTE) {
            AllNotesScreen(
                notes = notes,
                onNoteClicked = { note: Note ->
                    navActions.navigateToEditNoteScreen(note.id)
                },
                onFabClicked = {
                    navActions.navigateToAddNoteScreen()
                },
                onNoteDeleted = { note: Note ->
                    notesViewModel.deleteNote(note)
                },
                onNoteRestored = { note: Note ->
                    notesViewModel.insertNote(note)
                }
            )
        }
        composable(route = AppNoteDestinations.ADD_NOTE_ROUTE) {
            AddNoteScreen(
                onFabClicked = { note: Note ->
                    notesViewModel.insertNote(note)
                    navActions.navigateToAllNotesScreen()
                }
            )
        }
        composable(
            route = AppNoteDestinations.EDIT_NOTE_ROUTE,
            arguments = listOf(
                navArgument(
                    name = AppNoteArguments.NOTE_ID_KEY,
                    builder = { type = NavType.IntType }
                )
            )
        ) { navBackStackEntry: NavBackStackEntry ->
            val noteId = navBackStackEntry.arguments?.getInt(AppNoteArguments.NOTE_ID_KEY)
            EditNoteScreen(
                note = notes.first { it.id == noteId },
                onFabClicked = { note: Note ->
                    notesViewModel.updateNote(note)
                    navActions.navigateToAllNotesScreen()
                }
            )
        }
    }
}