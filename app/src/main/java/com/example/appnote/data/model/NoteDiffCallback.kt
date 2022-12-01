package com.example.appnote.data.model

import androidx.recyclerview.widget.DiffUtil

object NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
}