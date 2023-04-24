package com.example.appnote.ui

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.appnote.R
import com.google.android.material.snackbar.Snackbar

class NoteTouchCallback(
    private val requiredContext: Context,
    private val requiredView: View,
    private val notesViewModel: NotesViewModel,
    private val notesAdapter: NotesAdapter
) : SimpleCallback(0, LEFT or RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.layoutPosition
        val note = notesAdapter.currentList[position]

        notesViewModel.deleteNote(note)

        Snackbar.make(requiredView, requiredContext.getString(R.string.note_snackbar_deleted_msg), Snackbar.LENGTH_SHORT).apply {
            setAction(requiredContext.getString(R.string.note_snackbar_undo_action)) { notesViewModel.insertNote(note) }
        }.show()
    }
}