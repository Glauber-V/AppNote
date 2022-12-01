package com.example.appnote.data.repository

import androidx.lifecycle.LiveData
import com.example.appnote.data.database.AppNoteDatabase
import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.model.Note
import kotlinx.coroutines.withContext

class DefaultAppNoteRepository(
    private val database: AppNoteDatabase,
    private val dispatcher: DispatcherProvider
) : AppNoteRepository {


    override suspend fun insert(note: Note) {
        runCatching {
            withContext(dispatcher.io) {
                database.getNoteDao().insert(note)
            }
        }
    }

    override suspend fun update(note: Note) {
        runCatching {
            withContext(dispatcher.io) {
                database.getNoteDao().update(note)
            }
        }
    }

    override suspend fun delete(note: Note) {
        runCatching {
            withContext(dispatcher.io) {
                database.getNoteDao().delete(note)
            }
        }
    }

    override fun getAllNotes(): LiveData<List<Note>> {
        return database.getNoteDao().getAllNotes()
    }
}