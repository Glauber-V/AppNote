package com.example.appnote.util.rules

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.appnote.data.database.AppNoteDatabase
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class AppNoteDatabaseRule : TestWatcher() {

    private lateinit var database: AppNoteDatabase

    override fun starting(description: Description) {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppNoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        super.starting(description)
    }

    override fun finished(description: Description) {
        database.close()
        super.finished(description)
    }

    fun getDatabaseInstance(): AppNoteDatabase = database
}