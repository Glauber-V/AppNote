package com.example.appnote.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appnote.data.database.AppNoteDatabase
import com.example.appnote.data.model.Note
import com.example.appnote.util.rules.AppNoteDatabaseRule
import com.example.appnote.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class NoteDaoLocalTest {

    @get:Rule(order = 0) var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule(order = 1) var appNoteDatabaseRule = AppNoteDatabaseRule()

    private lateinit var database: AppNoteDatabase

    @Before
    fun setUp() {
        database = appNoteDatabaseRule.getDatabaseInstance()
    }

    @Test
    fun createNote_getNewNoteFromDatabase() = runTest {
        val note = Note(id = 22, title = "Note 22", content = "This is the note 22")
        database.getNoteDao().insert(note)
        advanceUntilIdle()

        val notes = database.getNoteDao().getAllNotes().getOrAwaitValue()

        assertThat(notes).isNotEmpty()
        assertThat(notes).contains(note)
    }

    @Test
    fun updateNote_verifyNoteWasUpdated() = runTest {
        var note = Note(id = 76, title = "Note 76", content = "This is the note 76")
        database.getNoteDao().insert(note)
        advanceUntilIdle()

        val updatedNote = note.copy(
            title = "Updated Note",
            content = "This is the updated note 76"
        )

        database.getNoteDao().update(updatedNote)
        advanceUntilIdle()

        val notes = database.getNoteDao().getAllNotes().getOrAwaitValue()
        note = notes.first()

        assertThat(note.title).isEqualTo("Updated Note")
        assertThat(note.content).isEqualTo("This is the updated note 76")
    }

    @Test
    fun deleteNote_verifyNoteWasDeleted() = runTest {
        val note = Note(id = 56, title = "Note 56", content = "This is the note 56")

        database.getNoteDao().insert(note)
        advanceUntilIdle()

        database.getNoteDao().delete(note)
        advanceUntilIdle()

        val notes = database.getNoteDao().getAllNotes().getOrAwaitValue()

        assertThat(notes).doesNotContain(note)
        assertThat(notes).isEmpty()
    }
}