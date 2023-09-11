package com.example.appnote.ui

import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
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
class EditNoteScreenTest {

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
    private lateinit var note: Note

    private val oldTitle = "Old Title"
    private val oldContent = "Old Content"
    private val newTitle = "New Title"
    private val newContent = "New Content"

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            notesViewModel = NotesViewModel(appNoteRepository, dispatcherProvider)
            notes = notesViewModel.notes.observeAsState(initial = emptyList())
            note = Note(id = 1, title = oldTitle, content = oldContent)
            EditNoteScreen(
                note = note,
                onFabClicked = { notesViewModel.updateNote(it) }
            )
        }
    }

    @Test
    fun onEditNoteScreen_editNote_verifySuccessfullyUpdated() = runTest {
        notesViewModel.insertNote(note)
        advanceUntilIdle()

        assertThat(notes.value).isNotEmpty()
        assertThat(notes.value).hasSize(1)
        assertThat(notes.value).contains(note)

        with(composeRule) {
            onRoot().printToLog(tag = "EditNoteScreen|NoteWithOldValues")

            onNodeWithText(note.title)
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(newTitle)

            onNodeWithText(note.content)
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(newContent)

            val saveNoteFabContentDesc = getStringResource(R.string.content_desc_save_note)
            onNodeWithContentDescription(saveNoteFabContentDesc).performClick()
            advanceUntilIdle()

            onRoot().printToLog(tag = "EditNoteScreen|NoteWithNewValues")

            assertThat(notes.value).isNotEmpty()
            assertThat(notes.value).hasSize(1)
            assertThat(notes.value.first().title).isEqualTo(newTitle)
            assertThat(notes.value.first().content).isEqualTo(newContent)

            onNodeWithText(newTitle)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(newContent)
                .assertExists()
                .assertIsDisplayed()
        }
    }
}