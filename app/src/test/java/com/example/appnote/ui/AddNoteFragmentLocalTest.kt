package com.example.appnote.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appnote.R
import com.example.appnote.di.AppNoteDatabaseModule
import com.example.appnote.di.DispatcherProviderModule
import com.example.appnote.util.getOrAwaitValue
import com.example.appnote.util.launchFragmentInHiltContainer
import com.example.appnote.util.rules.NavHostControllerRule
import com.example.appnote.util.rules.StandardTestDispatcherRule
import com.google.common.truth.Truth.*
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
class AddNoteFragmentLocalTest {

    @get:Rule(order = 0) var hiltAndroidRule = HiltAndroidRule(this)
    @get:Rule(order = 1) var standardTestDispatcherRule = StandardTestDispatcherRule()
    @get:Rule(order = 2) var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule(order = 3) var navHostControllerRule = NavHostControllerRule(currentDestination = R.id.add_note_fragment)

    private lateinit var testNavHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        testNavHostController = navHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchFragment_clickFab_addNote_verifyNoteWasAdded() = runTest {
        launchFragmentInHiltContainer<AddNoteFragment>(navHostController = testNavHostController) {

            onView(withId(R.id.add_note_frag_note_title))
                .perform(replaceText("title"))

            onView(withId(R.id.add_note_frag_note_content))
                .perform(replaceText("content"))

            onFabClicked()
            advanceUntilIdle()

            val notes = notesViewModel.notes.getOrAwaitValue()
            assertThat(notes).isNotEmpty()

            val savedNote = notes.first()
            assertThat(savedNote.id).isNotEqualTo(0)
            assertThat(savedNote.title).isEqualTo("title")
            assertThat(savedNote.content).isEqualTo("content")
        }
    }
}