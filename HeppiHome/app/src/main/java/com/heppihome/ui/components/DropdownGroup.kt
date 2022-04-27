package com.heppihome.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun DropDown() {
    var expanded by remember { mutableStateOf(false) }
    
    DropdownMenu(expanded = true, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(onClick = { println("New Group") }) {
            Text("New Group")
        }
        DropdownMenuItem(onClick = { println("Join Group") }) {
            Text("Join Group")
        }
        DropdownMenuItem(onClick = { println("Leave Group") }) {
            Text("Leave Group")
        }
    }
    
}
