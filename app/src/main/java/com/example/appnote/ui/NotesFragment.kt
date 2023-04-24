package com.example.appnote.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.appnote.R
import com.example.appnote.data.model.Note
import com.example.appnote.databinding.FragmentNotesBinding
import com.google.android.material.transition.platform.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment(), NoteClickListener, FabClickListener {

    private lateinit var binding: FragmentNotesBinding
    lateinit var notesViewModel: NotesViewModel
    lateinit var notesAdapter: NotesAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        notesViewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        notesAdapter = NotesAdapter(this)

        binding.notesRecyclerView.apply {
            val noteTouchCallback = NoteTouchCallback(requireContext(), binding.root, notesViewModel, notesAdapter)
            ItemTouchHelper(noteTouchCallback).attachToRecyclerView(this)
            adapter = notesAdapter
        }

        notesViewModel.notes.observe(viewLifecycleOwner) { hasNotes ->
            hasNotes?.let { notes ->
                notesAdapter.submitList(notes)
            }
        }
    }

    override fun onNoteClicked(cardView: View, note: Note) {
        notesViewModel.selectNote(note)
        val toEditNoteFragment = NotesFragmentDirections.actionNotesFragmentToEditNoteFragment()
        val transitionName = getString(R.string.note_edit_transition_name)
        val withExtras = FragmentNavigatorExtras(cardView to transitionName)
        findNavController().navigate(toEditNoteFragment, withExtras)
    }

    override fun onFabClicked() {
        val toAddNoteFragment = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment()
        findNavController().navigate(toAddNoteFragment)
    }
}