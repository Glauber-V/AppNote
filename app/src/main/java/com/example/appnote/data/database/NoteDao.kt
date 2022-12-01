package com.example.appnote.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appnote.data.model.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("select * from notes_table")
    fun getAllNotes(): LiveData<List<Note>>
}