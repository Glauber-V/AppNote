package com.example.appnote.data.repository

import androidx.lifecycle.LiveData
import com.example.appnote.data.database.AppNoteDatabase
import com.example.appnote.data.model.Note

class DefaultAppNoteRepository(private val database: AppNoteDatabase) : AppNoteRepository {


    override suspend fun insert(note: Note) {
        database.getNoteDao().insert(note)
    }

    override suspend fun update(note: Note) {
        database.getNoteDao().update(note)
    }

    override suspend fun delete(note: Note) {
        database.getNoteDao().delete(note)
    }

    override fun getAllNotes(): LiveData<List<Note>> {
        return database.getNoteDao().getAllNotes()
    }
}