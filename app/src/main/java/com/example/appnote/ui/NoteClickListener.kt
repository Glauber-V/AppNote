package com.example.appnote.ui

import android.view.View
import com.example.appnote.data.model.Note

interface NoteClickListener {

    fun onNoteClicked(cardView: View, note: Note)
}