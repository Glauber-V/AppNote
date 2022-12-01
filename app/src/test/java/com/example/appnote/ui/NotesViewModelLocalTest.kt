package com.example.appnote.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appnote.data.database.AppNoteDatabase
import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.dispatchers.FakeDispatcherProvider
import com.example.appnote.data.model.Note
import com.example.appnote.data.repository.AppNoteRepository
import com.example.appnote.data.repository.DefaultAppNoteRepository
import com.example.appnote.util.getOrAwaitValue
import com.example.appnote.util.rules.AppNoteDatabaseRule
import com.example.appnote.util.rules.StandardTestDispatcherRule
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
class NotesViewModelLocalTest {

    @get:Rule(order = 0) var standardTestDispatcherRule = StandardTestDispatcherRule()
    @get:Rule(order = 1) var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule(order = 2) var appNoteDatabaseRule = AppNoteDatabaseRule()

    private lateinit var database: AppNoteDatabase
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var notesRepository: AppNoteRepository

    @Before
    fun setUp() {
        database = appNoteDatabaseRule.getDatabaseInstance()
        dispatcherProvider = FakeDispatcherProvider()
        notesRepository = DefaultAppNoteRepository(database, dispatcherProvider)
    }

    @Test
    fun createNote_observeNotesList_returnSavedNote() = runTest {
        val note = Note(id = 49, title = "Note 49", content = "This is the note 49")
        val notesViewModel = NotesViewModel(notesRepository)
        notesViewModel.insertNote(note)
        advanceUntilIdle()

        val notes = notesViewModel.notes.getOrAwaitValue()

        assertThat(notes).contains(note)
    }

    @Test
    fun updateNote_verifyNoteWasUpdated() = runTest {
        var note = Note(id = 47, title = "Note 47", content = "This is the note 47")
        val notesViewModel = NotesViewModel(notesRepository)
        notesViewModel.insertNote(note)
        advanceUntilIdle()

        var notes = notesViewModel.notes.getOrAwaitValue()
        assertThat(notes).isNotEmpty()
        assertThat(notes).hasSize(1)

        val updatedNote = note.copy(title = "Updated note 47")
        notesViewModel.updateNote(updatedNote)
        advanceUntilIdle()

        notes = notesViewModel.notes.getOrAwaitValue()
        note = notes.first()

        assertThat(note.title).isEqualTo("Updated note 47")
    }

    @Test
    fun deleteNote_verifyNoteWasDeleted() = runTest {
        val note = Note(id = 47, title = "Note 47", content = "This is the note 47")
        val notesViewModel = NotesViewModel(notesRepository)
        notesViewModel.insertNote(note)
        advanceUntilIdle()

        var notes = notesViewModel.notes.getOrAwaitValue()
        assertThat(notes).isNotEmpty()
        assertThat(notes).hasSize(1)

        notesViewModel.deleteNote(note)
        advanceUntilIdle()

        notes = notesViewModel.notes.getOrAwaitValue()
        assertThat(notes).doesNotContain(note)
        assertThat(notes).isEmpty()
    }
}