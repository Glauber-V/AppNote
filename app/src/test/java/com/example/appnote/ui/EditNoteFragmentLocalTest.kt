package com.example.appnote.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.appnote.R
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
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@UninstallModules(
    DispatcherProviderModule::class,
    AppNoteDatabaseModule::class
)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class EditNoteFragmentLocalTest {

    @get:Rule(order = 0) var hiltAndroidRule = HiltAndroidRule(this)
    @get:Rule(order = 1) var standardTestDispatcherRule = StandardTestDispatcherRule()
    @get:Rule(order = 2) var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule(order = 3) var navHostControllerRule = NavHostControllerRule(currentDestination = R.id.edit_note_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = navHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchFragment_clickFab_updateNote_verifyNoteWasUpdated() = runTest {
        launchFragmentInHiltContainer<EditNoteFragment>(navHostController = navHostController) {

            val note = Note(id = 36, title = "Note 36", content = "This is the note 36")
            notesViewModel.insertNote(note)
            advanceUntilIdle()

            notesViewModel.selectNote(note)

            val selectedNote = notesViewModel.selectedNote.getOrAwaitValue()
            assertThat(selectedNote).isNotNull()
            assertThat(selectedNote).isEqualTo(note)

            onView(withId(R.id.edit_note_frag_note_title))
                .perform(replaceText("Updated title"))

            onView(withId(R.id.edit_note_frag_note_content))
                .perform(replaceText("Updated content"))

            onFabClicked()
            advanceUntilIdle()

            val notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).isNotEmpty()

            val updatedNote = notes.first()
            assertThat(updatedNote.title).isEqualTo("Updated title")
            assertThat(updatedNote.content).isEqualTo("Updated content")
        }
    }
}