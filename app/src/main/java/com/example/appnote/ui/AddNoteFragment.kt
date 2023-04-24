package com.example.appnote.ui

import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appnote.R
import com.example.appnote.data.model.Note
import com.example.appnote.databinding.FragmentAddNoteBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransform

class AddNoteFragment : Fragment(), FabClickListener {

    private lateinit var binding: FragmentAddNoteBinding
    lateinit var notesViewModel: NotesViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab)
            endView = binding.addNoteRoot
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            setAllContainerColors(Color.WHITE)
        }

        returnTransition = Slide().apply {
            duration = resources.getInteger(R.integer.motion_duration_small).toLong()
            addTarget(binding.addNoteRoot)
        }
    }

    override fun onFabClicked() {
        if (verifyNote()) {
            val noteTitle = binding.addNoteFragNoteTitle.text.toString()
            val noteContent = binding.addNoteFragNoteContent.text.toString()
            val newNote = Note(title = noteTitle, content = noteContent)
            notesViewModel.insertNote(newNote)
            findNavController().popBackStack()
        }
    }


    private fun verifyNote(): Boolean {
        val isValid = binding.addNoteFragNoteTitle.text.toString().isNotEmpty()
        val errorMessage = getString(R.string.note_warning_no_title)
        if (!isValid) Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        return isValid
    }
}