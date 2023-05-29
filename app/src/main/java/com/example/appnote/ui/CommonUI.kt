package com.example.appnote.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.appnote.R
import com.example.appnote.ui.theme.AppNoteTheme

@Composable
fun NoteDetail(
    noteTitle: String,
    onNoteTitleChanged: (String) -> Unit,
    noteContent: String,
    onNoteContentChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TextField(
            value = noteTitle,
            onValueChange = onNoteTitleChanged,
            placeholder = {
                Text(
                    text = stringResource(R.string.note_hint_title),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            textStyle = MaterialTheme.typography.titleLarge,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = noteContent,
            onValueChange = onNoteContentChanged,
            placeholder = {
                Text(
                    text = stringResource(R.string.note_hint_content),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewNoteDetail() {
    AppNoteTheme {
        NoteDetail(
            noteTitle = "Example note title",
            noteContent = "This is an example note content",
            onNoteTitleChanged = {},
            onNoteContentChanged = {}
        )
    }
}