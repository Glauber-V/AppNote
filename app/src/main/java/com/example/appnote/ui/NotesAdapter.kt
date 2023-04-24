package com.example.appnote.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.appnote.data.model.Note
import com.example.appnote.data.model.NoteDiffCallback
import com.example.appnote.databinding.ItemNoteBinding
import com.example.appnote.ui.NotesAdapter.NoteViewHolder

class NotesAdapter(private val listener: NoteClickListener) : ListAdapter<Note, NoteViewHolder>(NoteDiffCallback) {

    class NoteViewHolder(private val binding: ItemNoteBinding) : ViewHolder(binding.root) {

        fun bind(note: Note, noteClickListener: NoteClickListener) {
            binding.note = note
            binding.noteClickListener = noteClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, listener)
    }
}