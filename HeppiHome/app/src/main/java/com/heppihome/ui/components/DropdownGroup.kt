package com.heppihome.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


var expanded = true

@Preview
@Composable
fun DropDown() {
    
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
