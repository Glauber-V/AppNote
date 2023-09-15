package com.example.appnote.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.appnote.R
import com.example.appnote.ui.theme.AppNoteTheme

@Composable
fun NoteDetail(
    modifier: Modifier = Modifier,
    noteTitle: String,
    onNoteTitleChanged: (String) -> Unit,
    noteContent: String,
    onNoteContentChanged: (String) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewNoteDetail() {
    AppNoteTheme {
        NoteDetail(
            noteTitle = stringResource(id = R.string.place_holder_note_title),
            noteContent = stringResource(id = R.string.place_holder_note_content),
            onNoteTitleChanged = {},
            onNoteContentChanged = {}
        )
    }
}