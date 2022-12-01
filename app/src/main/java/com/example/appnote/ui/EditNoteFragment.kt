package com.example.appnote.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appnote.R
import com.example.appnote.data.model.Note
import com.example.appnote.databinding.FragmentEditNoteBinding
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    lateinit var notesViewModel: NotesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            setAllContainerColors(Color.WHITE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.updateNoteFab.show()

        notesViewModel.selectedNote.observe(viewLifecycleOwner) { hasNote ->
            hasNote?.let { note ->
                binding.note = note
            }
        }

        binding.updateNoteFab.setOnClickListener {
            binding.updateNoteFab.hide()
            val note = binding.note ?: Note()
            val updatedTitle = binding.editNoteFragNoteTitle.text.toString()
            val updatedContent = binding.editNoteFragNoteContent.text.toString()
            val updatedNote = note.copy(title = updatedTitle, content = updatedContent)
            notesViewModel.updateNote(updatedNote)
            notesViewModel.unselectNote()
            findNavController().navigateUp()
        }
    }
}