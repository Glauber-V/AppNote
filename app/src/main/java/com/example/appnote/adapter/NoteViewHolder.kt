package com.example.appnote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appnote.data.model.Note
import com.example.appnote.databinding.ItemNoteBinding

class NoteViewHolder(
    private val binding: ItemNoteBinding,
    listener: NotesAdapter.NotesListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    companion object {
        fun from(parent: ViewGroup, listener: NotesAdapter.NotesListener): NoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
            return NoteViewHolder(binding, listener)
        }
    }

    fun bind(note: Note) {
        binding.note = note
        binding.executePendingBindings()
    }
}