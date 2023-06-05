package com.example.appnote.data.database

import androidx.room.*
import com.example.appnote.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("select * from notes_table")
    fun getAllNotes(): Flow<List<Note>>
}