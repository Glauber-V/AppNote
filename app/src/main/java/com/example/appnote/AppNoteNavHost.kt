package com.example.appnote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appnote.data.model.Note
import com.example.appnote.ui.AddNoteScreen
import com.example.appnote.ui.AllNotesScreen
import com.example.appnote.ui.EditNoteScreen
import com.example.appnote.ui.NotesViewModel

@Composable
fun AppNoteNavHost(
    navController: NavHostController,
    startDestination: String,
    notesViewModel: NotesViewModel,
    modifier: Modifier = Modifier
) {
    val notes by notesViewModel.notes.observeAsState(initial = emptyList())
    val selectedNote by notesViewModel.selectedNote.observeAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = AllNotesDestination.route) {
            AllNotesScreen(
                notes = notes,
                onNoteClicked = { note: Note ->
                    notesViewModel.selectNote(note)
                    navController.navigate(EditNoteDestination.route)
                },
                onFabClicked = {
                    navController.navigate(AddNoteDestination.route)
                },
                onNoteDeleted = { note: Note ->
                    notesViewModel.deleteNote(note)
                },
                onNoteRestored = { note: Note ->
                    notesViewModel.insertNote(note)
                }
            )
        }
        composable(route = AddNoteDestination.route) {
            AddNoteScreen(
                onFabClicked = { note: Note ->
                    notesViewModel.insertNote(note)
                    navController.popBackStack()
                }
            )
        }
        composable(route = EditNoteDestination.route) {
            if (selectedNote != null) {
                EditNoteScreen(
                    note = selectedNote!!,
                    onFabClicked = { note: Note ->
                        notesViewModel.updateNote(note)
                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}