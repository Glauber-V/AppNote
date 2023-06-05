package com.example.appnote.data.repository

import com.example.appnote.data.model.Note
import kotlinx.coroutines.flow.Flow

interface AppNoteRepository {

    suspend fun insert(note: Note)

    suspend fun update(note: Note)

    suspend fun delete(note: Note)

    fun getAllNotes(): Flow<List<Note>>
}