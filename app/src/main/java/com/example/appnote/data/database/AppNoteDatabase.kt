package com.example.appnote.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appnote.data.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppNoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
}