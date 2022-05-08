package com.heppihome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDialog(title: String? = null, content: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onDismiss()
        },
        /*
        // https://stackoverflow.com/questions/69452854/increase-space-between-title-and-text-of-alertdialog-in-compose
        title = {
           if (!title.isNullOrEmpty()) {
               Text(title)
           }
        },
         */
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                // horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // CircularProgressIndicator()
                // Text("Hello")
                if (!title.isNullOrEmpty()) {
                    Text(title,
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.subtitle1)
                }

                Text(content)
                // Timber.d("message=${progressState.message}")
            }
        },
        // buttons = { }
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("No")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("Yes")
            }
        }
    )
}