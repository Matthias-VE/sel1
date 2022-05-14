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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.heppihome.R

@Composable
fun ConfirmDialog(title: String? = null, content: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onDismiss()
        },

        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

            ) {

                if (!title.isNullOrEmpty()) {
                    Text(title,
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.subtitle1)
                }

                Text(content)

            }
        },

        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(stringResource(R.string.No))
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(stringResource(R.string.Yes))
            }
        }
    )
}