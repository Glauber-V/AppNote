package com.example.appnote.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appnote.R
import com.example.appnote.data.model.Note
import com.example.appnote.ui.theme.AppNoteTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNotesScreen(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onFabClicked: () -> Unit,
    onNoteClicked: (Note) -> Unit,
    onNoteDeleted: (Note) -> Unit,
    onNoteRestored: (Note) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClicked,
                shape = RoundedCornerShape(100),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Rounded.Create,
                    contentDescription = stringResource(id = R.string.content_desc_write_new_note)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        LazyColumn(modifier = Modifier.padding(contentPadding)) {
            items(
                items = notes,
                key = { it.id }
            ) { note: Note ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { swipeToDismissBoxValue ->
                        if (swipeToDismissBoxValue == SwipeToDismissBoxValue.EndToStart) {
                            onNoteDeleted(note)
                            scope.launch {
                                val snackbarActionResult = snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.note_snackbar_deleted_msg),
                                    actionLabel = context.getString(R.string.note_snackbar_undo_action)
                                )
                                when (snackbarActionResult) {
                                    SnackbarResult.ActionPerformed -> onNoteRestored(note)
                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        }
                        swipeToDismissBoxValue != SwipeToDismissBoxValue.EndToStart
                    }
                )
                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromEndToStart = true,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {}
                ) {
                    NoteItem(
                        noteTitle = note.title,
                        noteContent = note.content,
                        onNoteClicked = { onNoteClicked(note) }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    noteTitle: String,
    noteContent: String,
    onNoteClicked: () -> Unit
) {
    Card(
        onClick = onNoteClicked,
        shape = RoundedCornerShape(size = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = noteTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = noteContent,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewAllNotesScreen() {
    AppNoteTheme {
        val content = stringResource(id = R.string.place_holder_note_content)
        AllNotesScreen(
            notes = listOf(
                Note(id = 1, title = "Note 1", content = content),
                Note(id = 2, title = "Note 2", content = content),
                Note(id = 3, title = "Note 3", content = content),
                Note(id = 4, title = "Note 4", content = content),
                Note(id = 5, title = "Note 5", content = content)
            ),
            onFabClicked = {},
            onNoteClicked = {},
            onNoteDeleted = {},
            onNoteRestored = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewNoteItem() {
    AppNoteTheme {
        NoteItem(
            noteTitle = stringResource(id = R.string.place_holder_note_title),
            noteContent = stringResource(id = R.string.place_holder_note_content),
            onNoteClicked = {}
        )
    }
}