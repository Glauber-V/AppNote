package com.example.appnote.data.repository

import androidx.lifecycle.LiveData
import com.example.appnote.data.model.Note

interface AppNoteRepository {

    suspend fun insert(note: Note)

    suspend fun update(note: Note)

    suspend fun delete(note: Note)

    fun getAllNotes(): LiveData<List<Note>>
}