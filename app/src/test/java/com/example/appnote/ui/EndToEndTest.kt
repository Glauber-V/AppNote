package com.example.appnote.ui

import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appnote.AppNoteApp
import com.example.appnote.AppNoteDestinations
import com.example.appnote.R
import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.repository.AppNoteRepository
import com.example.appnote.di.AppNoteDatabaseModule
import com.example.appnote.di.DispatcherProviderModule
import com.example.appnote.ui.theme.AppNoteTheme
import com.example.appnote.util.assertCurrentRouteIsEqualTo
import com.example.appnote.util.getStringResource
import com.example.appnote.util.rules.StandardTestDispatcherRule
import com.example.appnote.util.showSemanticTreeInConsole
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
class EndToEndTest {

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
    private lateinit var navHostController: NavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            AppNoteTheme {
                navHostController = rememberNavController()
                notesViewModel = NotesViewModel(appNoteRepository, dispatcherProvider)
                AppNoteApp(navHostController = navHostController, notesViewModel = notesViewModel)
            }
        }
    }

    @Test
    fun onAppNoteApp_createNote_updateNote_deleteNote_restoreNote() = runTest {
        val oldNoteTitle = "Old note title"
        val oldNoteContent = "Old note content"

        val newNoteTitle = "New note title"
        val newNoteContent = "New note content"

        with(composeRule) {
            navHostController.assertCurrentRouteIsEqualTo(AppNoteDestinations.ALL_NOTES_ROUTE)

            onNodeWithText(oldNoteTitle).assertDoesNotExist()

            onNodeWithText(oldNoteContent).assertDoesNotExist()

            val allNotesFabContentDesc = getStringResource(R.string.content_desc_write_new_note)
            onNodeWithContentDescription(allNotesFabContentDesc)
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            navHostController.assertCurrentRouteIsEqualTo(AppNoteDestinations.ADD_NOTE_ROUTE)

            val noteHintTitle = getStringResource(R.string.note_hint_title)
            onNodeWithText(noteHintTitle)
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(oldNoteTitle)

            val noteHintContent = getStringResource(R.string.note_hint_content)
            onNodeWithText(noteHintContent)
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(oldNoteContent)

            val addNoteFabContentDesc = getStringResource(R.string.content_desc_save_note)
            onNodeWithContentDescription(addNoteFabContentDesc)
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            advanceUntilIdle()

            navHostController.assertCurrentRouteIsEqualTo(AppNoteDestinations.ALL_NOTES_ROUTE)

            onNodeWithText(oldNoteTitle)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(oldNoteContent)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(oldNoteTitle)
                .assertIsDisplayed()
                .performClick()

            navHostController.assertCurrentRouteIsEqualTo(AppNoteDestinations.EDIT_NOTE_ROUTE)

            onNodeWithText(oldNoteTitle)
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(newNoteTitle)

            onNodeWithText(oldNoteContent)
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(newNoteContent)

            val editNoteFabContentDesc = getStringResource(R.string.content_desc_save_note)
            onNodeWithContentDescription(editNoteFabContentDesc)
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            advanceUntilIdle()

            onNodeWithText(oldNoteTitle).assertDoesNotExist()

            onNodeWithText(oldNoteContent).assertDoesNotExist()

            onNodeWithText(newNoteTitle)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(newNoteContent)
                .assertExists()
                .assertIsDisplayed()

            val snackbarActionText = getStringResource(R.string.note_snackbar_undo_action)
            onNodeWithText(snackbarActionText).assertDoesNotExist()

            onNodeWithText(newNoteTitle).performTouchInput { swipeLeft() }
            advanceUntilIdle()

            onNodeWithText(newNoteTitle).assertDoesNotExist()

            onNodeWithText(newNoteContent).assertDoesNotExist()

            onNodeWithText(snackbarActionText)
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            advanceUntilIdle()

            onNodeWithText(newNoteTitle)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(newNoteContent)
                .assertExists()
                .assertIsDisplayed()
        }
    }
}