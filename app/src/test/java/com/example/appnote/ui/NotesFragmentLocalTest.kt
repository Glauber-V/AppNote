package com.example.appnote.ui

import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appnote.R
import com.example.appnote.adapter.NoteViewHolder
import com.example.appnote.data.model.Note
import com.example.appnote.di.AppNoteDatabaseModule
import com.example.appnote.di.DispatcherProviderModule
import com.example.appnote.util.getOrAwaitValue
import com.example.appnote.util.launchFragmentInHiltContainer
import com.example.appnote.util.rules.NavHostControllerRule
import com.example.appnote.util.rules.StandardTestDispatcherRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@UninstallModules(
    AppNoteDatabaseModule::class,
    DispatcherProviderModule::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class NotesFragmentLocalTest {

    @get:Rule(order = 0) var hiltAndroidRule = HiltAndroidRule(this)
    @get:Rule(order = 1) var standardTestDispatcherRule = StandardTestDispatcherRule()
    @get:Rule(order = 2) var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule(order = 3) var navHostControllerRule = NavHostControllerRule(
        navGraphId = R.navigation.nav_graph,
        currentDestination = R.id.notes_fragment
    )

    private lateinit var testNavHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        testNavHostController = navHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchFragment_add3Notes_verifyAdapterListSizeIs3() = runTest {
        launchFragmentInHiltContainer<NotesFragment>(navHostController = testNavHostController) {
            val firstNote = Note(title = "First note")
            notesViewModel.insertNote(firstNote)
            advanceUntilIdle()

            val secondNote = Note(title = "Second note")
            notesViewModel.insertNote(secondNote)
            advanceUntilIdle()

            val thirdNote = Note(title = "Third note")
            notesViewModel.insertNote(thirdNote)
            advanceUntilIdle()

            val notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).hasSize(3)

            shadowOf(getMainLooper()).idle()
            assertThat(notesAdapter).isNotNull()
            assertThat(notesAdapter.currentList).isNotEmpty()
            assertThat(notesAdapter.currentList.size).isEqualTo(3)
        }
    }

    @Test
    fun launchFragment_clickOnFirstNote_navigateToEditNoteFragment() = runTest {
        launchFragmentInHiltContainer<NotesFragment>(navHostController = testNavHostController) {
            val note = Note(id = 36, title = "Note 36", content = "This is the note 36")
            notesViewModel.insertNote(note)
            advanceUntilIdle()

            val notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).isNotEmpty()
            assertThat(notes).hasSize(1)

            onView(withId(R.id.notes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition<NoteViewHolder>(0, click()))

            val selectedNote = notesViewModel.selectedNote.getOrAwaitValue()
            assertThat(selectedNote).isEqualTo(note)

            assertThat(testNavHostController.currentDestination?.id).isEqualTo(R.id.edit_note_fragment)
        }
    }

    @Test
    fun launchFragment_clickCreateNoteFab_navigateToAddNoteFragment() {
        launchFragmentInHiltContainer<NotesFragment>(navHostController = testNavHostController) {

            onView(withId(R.id.create_note_fab))
                .perform(click())

            assertThat(testNavHostController.currentDestination?.id).isEqualTo(R.id.add_note_fragment)
        }
    }

    @Test
    fun launchFragment_swipeLeft_noteShouldBeDeleted() = runTest {
        launchFragmentInHiltContainer<NotesFragment>(navHostController = testNavHostController) {
            val note = Note(id = 1, title = "Note", content = "Content")
            notesViewModel.insertNote(note)
            advanceUntilIdle()

            var notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).contains(note)

            onView(withId(R.id.notes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition<NoteViewHolder>(0, swipeLeft()))
            advanceUntilIdle()

            notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).isEmpty()
        }
    }
}