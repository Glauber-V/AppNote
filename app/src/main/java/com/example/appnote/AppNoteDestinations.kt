package com.example.appnote

interface AppNoteDestination {
    val route: String
}

object AllNotesDestination : AppNoteDestination {
    override val route: String
        get() = "allNotes"
}

object AddNoteDestination : AppNoteDestination {
    override val route: String
        get() = "addNote"
}

object EditNoteDestination : AppNoteDestination {
    override val route: String
        get() = "editNote"
}