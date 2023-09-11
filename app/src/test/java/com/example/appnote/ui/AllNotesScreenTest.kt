package com.example.appnote.ui

import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeLeft
import com.example.appnote.R
import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.model.Note
import com.example.appnote.data.repository.AppNoteRepository
import com.example.appnote.di.AppNoteDatabaseModule
import com.example.appnote.di.DispatcherProviderModule
import com.example.appnote.util.getStringResource
import com.example.appnote.util.rules.StandardTestDispatcherRule
import com.example.appnote.util.showSemanticTreeInConsole
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
import javax.inject.Inject

@UninstallModules(DispatcherProviderModule::class, AppNoteDatabaseModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class AllNotesScreenTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val standardTestDispatcherRule = StandardTestDispatcherRule()

    @get:Rule(order = 2)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 3)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    lateinit var appNoteRepository: AppNoteRepository

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notes: State<List<Note>>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            notesViewModel = NotesViewModel(appNoteRepository, dispatcherProvider)
            notes = notesViewModel.notes.observeAsState(initial = emptyList())
            AllNotesScreen(
                notes = notes.value,
                onFabClicked = {},
                onNoteClicked = {},
                onNoteDeleted = { notesViewModel.deleteNote(it) },
                onNoteRestored = { notesViewModel.insertNote(it) }
            )
        }
    }

    @Test
    fun onAllNotesScreen_write3Notes_verifyNotesAreDisplayed() = runTest {
        assertThat(notes.value).isEmpty()

        val note1 = Note(id = 1, title = "Note 1", content = "Note 1 Content")
        notesViewModel.insertNote(note1)
        advanceUntilIdle()

        val note2 = Note(id = 2, title = "Note 2", content = "Note 2 Content")
        notesViewModel.insertNote(note2)
        advanceUntilIdle()

        val note3 = Note(id = 3, title = "Note 3", content = "Note 3 Content")
        notesViewModel.insertNote(note3)
        advanceUntilIdle()

        assertThat(notes.value).isNotEmpty()
        assertThat(notes.value).hasSize(3)

        with(composeRule) {
            onRoot().printToLog(tag = "Write3NotesTest")

            onNodeWithText("Note 0").assertDoesNotExist()

            onNodeWithText(note1.title)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(note2.title)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(note3.title)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText("Note 4").assertDoesNotExist()
        }
    }

    @Test
    fun onAllNotesScreen_swipeNoteToStart_verifySuccessfullyDeleted() = runTest {
        assertThat(notes.value).isEmpty()

        val note = Note(id = 1, title = "Note", content = "Note Content")
        notesViewModel.insertNote(note)
        advanceUntilIdle()

        assertThat(notes.value).isNotEmpty()
        assertThat(notes.value).hasSize(1)
        assertThat(notes.value).contains(note)

        with(composeRule) {
            onRoot().printToLog(tag = "SwipeNoteToStart")

            onNodeWithText(note.title)
                .assertExists()
                .assertIsDisplayed()
                .performTouchInput { swipeLeft() }

            advanceUntilIdle()

            onNodeWithText(note.title).assertDoesNotExist()

            assertThat(notes.value).isEmpty()
        }
    }

    @Test
    fun onAllNotesScreen_swipeNoteToStart_verifySuccessfullyDeleted_restoreNoteWithSnackbarAction() = runTest {
        assertThat(notes.value).isEmpty()

        val note = Note(id = 1, title = "Note", content = "Note Content")
        notesViewModel.insertNote(note)
        advanceUntilIdle()

        assertThat(notes.value).isNotEmpty()
        assertThat(notes.value).hasSize(1)
        assertThat(notes.value).contains(note)

        with(composeRule) {
            onRoot().printToLog(tag = "SwipeNoteToStart")

            val snackbarActionText = getStringResource(R.string.note_snackbar_undo_action)
            onNodeWithText(snackbarActionText).assertDoesNotExist()

            onNodeWithText(note.title)
                .assertExists()
                .assertIsDisplayed()
                .performTouchInput { swipeLeft() }

            advanceUntilIdle()

            onNodeWithText(note.title).assertDoesNotExist()

            assertThat(notes.value).isEmpty()

            onRoot().printToLog(tag = "SwipeNoteToStart|Snackbar")

            onNodeWithText(snackbarActionText)
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            advanceUntilIdle()

            assertThat(notes.value).isNotEmpty()
            assertThat(notes.value).hasSize(1)
            assertThat(notes.value).contains(note)

            onNodeWithText(note.title)
                .assertExists()
                .assertIsDisplayed()
        }
    }
}
