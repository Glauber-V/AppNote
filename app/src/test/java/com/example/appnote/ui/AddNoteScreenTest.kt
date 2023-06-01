package com.example.appnote.ui

import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.example.appnote.data.repository.AppNoteRepository
import com.example.appnote.di.AppNoteDatabaseModule
import com.example.appnote.di.DispatcherProviderModule
import com.example.appnote.util.getOrAwaitValue
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@UninstallModules(DispatcherProviderModule::class, AppNoteDatabaseModule::class)
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class AddNoteScreenTest {

    @get:Rule(order = 0) val hiltAndroidRule = HiltAndroidRule(this)
    @get:Rule(order = 1) val standardTestDispatcherRule = StandardTestDispatcherRule()
    @get:Rule(order = 2) val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule(order = 3) val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Inject lateinit var appNoteRepository: AppNoteRepository
    @Inject lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var notesViewModel: NotesViewModel

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            notesViewModel = NotesViewModel(appNoteRepository, dispatcherProvider)
            AddNoteScreen(
                onFabClicked = { notesViewModel.insertNote(it) }
            )
        }
    }

    @Test
    fun onAddNoteScreen_writeNewNote_verifySuccessfullyInserted() = runTest {
        composeRule.apply {
            var notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).isEmpty()

            onRoot().printToLog(tag = "WriteNewNote|EmptyNote")

            val noteHintTitle = getStringResource(R.string.note_hint_title)
            onNodeWithText(noteHintTitle).assertIsDisplayed()

            val noteHintContent = getStringResource(R.string.note_hint_content)
            onNodeWithText(noteHintContent).assertIsDisplayed()

            val noteTitle = "Shinny Note"
            onNodeWithText(noteHintTitle)
                .assertIsDisplayed()
                .performTextReplacement(noteTitle)

            val noteContent = "Shinny Note Content"
            onNodeWithText(noteHintContent)
                .assertIsDisplayed()
                .performTextReplacement(noteContent)

            val addNoteFabContentDesc = getStringResource(R.string.content_desc_save_note)
            onNodeWithContentDescription(addNoteFabContentDesc).performClick()
            advanceUntilIdle()

            onRoot().printToLog(tag = "WriteNewNote|FilledNote")

            notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).isNotEmpty()
            assertThat(notes).hasSize(1)
            assertThat(notes.first().title).isEqualTo(noteTitle)
            assertThat(notes.first().content).isEqualTo(noteContent)

            onNodeWithText(noteTitle)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(noteContent)
                .assertExists()
                .assertIsDisplayed()
        }
    }
}