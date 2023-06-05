package com.example.appnote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.model.Note
import com.example.appnote.data.repository.AppNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val appNoteRepository: AppNoteRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val notes: LiveData<List<Note>> = appNoteRepository.getAllNotes()
        .flowOn(dispatcherProvider.io)
        .asLiveData()


    fun insertNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.io) {
            appNoteRepository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.io) {
            appNoteRepository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.io) {
            appNoteRepository.delete(note)
        }
    }
}