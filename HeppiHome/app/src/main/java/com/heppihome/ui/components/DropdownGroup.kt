package com.heppihome.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.heppihome.R


@Preview
@Composable
fun DropDown() {
    var expanded by remember { mutableStateOf(false) }
    
    DropdownMenu(expanded = true, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(onClick = { println("New Group") }) {
            Text(stringResource(R.string.NewGroup))
        }
        DropdownMenuItem(onClick = { println("Join Group") }) {
            Text(stringResource(R.string.JoinGroup))
        }
        DropdownMenuItem(onClick = { println("Leave Group") }) {
            Text(stringResource(R.string.LeaveGroup))
        }
    }
    
}
