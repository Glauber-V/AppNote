package com.example.appnote.util.rules

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.appnote.data.database.AppNoteDatabase
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class AppNoteDatabaseRule : TestWatcher() {

    private lateinit var database: AppNoteDatabase

    override fun starting(description: Description) {
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppNoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    override fun finished(description: Description) {
        database.close()
    }

    fun getDatabaseInstance(): AppNoteDatabase = database
}