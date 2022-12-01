package com.example.appnote.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.appnote.data.model.Note
import com.example.appnote.data.model.NoteDiffCallback

class NotesAdapter(private val listener: NotesListener) : ListAdapter<Note, NoteViewHolder>(NoteDiffCallback) {

    interface NotesListener {
        fun onNoteClicked(cardView: View, note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }
}