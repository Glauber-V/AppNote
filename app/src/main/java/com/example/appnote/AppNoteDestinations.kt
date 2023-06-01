package com.example.appnote

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.appnote.AppNoteArguments.NOTE_ID_KEY
import com.example.appnote.AppNoteScreens.ADD_NOTE_SCREEN
import com.example.appnote.AppNoteScreens.ALL_NOTES_SCREEN
import com.example.appnote.AppNoteScreens.EDIT_NOTE_SCREEN

private object AppNoteScreens {
    const val ALL_NOTES_SCREEN = "allNotes"
    const val ADD_NOTE_SCREEN = "addNote"
    const val EDIT_NOTE_SCREEN = "editNote"
}

object AppNoteArguments {
    const val NOTE_ID_KEY = "noteId"
}

object AppNoteDestinations {
    const val ALL_NOTES_ROUTE = ALL_NOTES_SCREEN
    const val ADD_NOTE_ROUTE = ADD_NOTE_SCREEN
    const val EDIT_NOTE_ROUTE = "$EDIT_NOTE_SCREEN/{$NOTE_ID_KEY}"
}

class AppNoteNavigationActions(private val navHostController: NavHostController) {

    fun navigateToAllNotesScreen() {
        navHostController.navigate(route = ALL_NOTES_SCREEN) {
            popUpTo(navHostController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }

    fun navigateToAddNoteScreen() {
        navHostController.navigate(route = ADD_NOTE_SCREEN) {
            launchSingleTop = true
        }
    }

    fun navigateToEditNoteScreen(noteId: Int) {
        navHostController.navigate(route = "$EDIT_NOTE_SCREEN/$noteId") {
            launchSingleTop = true
        }
    }
}