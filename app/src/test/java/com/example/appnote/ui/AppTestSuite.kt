package com.example.appnote.ui

import com.example.appnote.data.NoteDaoLocalTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    NoteDaoLocalTest::class,
    NotesViewModelLocalTest::class,
    NotesFragmentLocalTest::class,
    AddNoteFragmentLocalTest::class,
    EditNoteFragmentLocalTest::class)
class AppTestSuite