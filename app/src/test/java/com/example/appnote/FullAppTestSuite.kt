package com.example.appnote

import com.example.appnote.data.NoteDaoLocalTest
import com.example.appnote.ui.AddNoteFragmentLocalTest
import com.example.appnote.ui.EditNoteFragmentLocalTest
import com.example.appnote.ui.NotesFragmentLocalTest
import com.example.appnote.ui.NotesViewModelLocalTest
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
class FullAppTestSuite