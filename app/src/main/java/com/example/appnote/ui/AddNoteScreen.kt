package com.example.appnote.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.appnote.R
import com.example.appnote.data.model.Note
import com.example.appnote.ui.theme.AppNoteTheme
import kotlinx.coroutines.launch

@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    onFabClicked: (Note) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isNotEmpty()) {
                        onFabClicked(Note(title = title, content = content))
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(message = context.getString(R.string.note_warning_no_title))
                        }
                    }
                },
                shape = RoundedCornerShape(100),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.content_desc_save_note)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        NoteDetail(
            modifier = Modifier.padding(contentPadding),
            noteTitle = title,
            onNoteTitleChanged = { title = it },
            noteContent = content,
            onNoteContentChanged = { content = it }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewAddNoteScreen() {
    AppNoteTheme {
        AddNoteScreen(onFabClicked = {})
    }
}