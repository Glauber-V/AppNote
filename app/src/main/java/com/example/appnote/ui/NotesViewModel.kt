package com.example.appnote.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnote.data.dispatchers.DispatcherProvider
import com.example.appnote.data.model.Note
import com.example.appnote.data.repository.AppNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val appNoteRepository: AppNoteRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val notes: LiveData<List<Note>> = appNoteRepository.getAllNotes()

    private val _selectedNote = MutableLiveData<Note?>()
    val selectedNote: LiveData<Note?> = _selectedNote


    fun insertNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.main) {
            appNoteRepository.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.main) {
            appNoteRepository.update(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.main) {
            appNoteRepository.delete(note)
        }
    }


    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    fun unselectNote() {
        _selectedNote.value = null
    }
}