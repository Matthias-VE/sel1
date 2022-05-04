package com.heppihome.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.viewmodels.groups.AddGroupViewModel

@Composable
fun NewGroup(
    vM : AddGroupViewModel,
    onGroupCancel : () -> Unit
) {
    val temp by vM.groupName.collectAsState()
    val temp2 by vM.description.collectAsState()

    Column() {
        Header(onGroupCancel)
        Column(modifier = Modifier
            .padding(10.dp)) {
            InputField(name = "Name Of Group", description = temp.text) { x -> vM.setGroup(x) }
            InputField(name = "Description", description = temp2.text) { x -> vM.setDescription(x) }
            Button(onClick = { vM.addGroups() },
                modifier = Modifier.padding(10.dp)) {
                Text("Add")
            }
        }
    }

}

@Composable
fun Header(onGroupCancel: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row() {
            Row(modifier = Modifier
                .padding(10.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = onGroupCancel) {
                    Icon(
                        Icons.Default.Close, contentDescription = "Cancel", modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
                Text("New Group", fontSize = 30.sp, textAlign = TextAlign.Center)
            }

        }
    }
}

@Composable
fun InputField(name: String, description: String, edit: (String) -> Unit) {

    Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
        Text(name, color = Color.Gray)
        OutlinedTextField(value = description, modifier = Modifier.fillMaxWidth(), onValueChange = { newText ->
            edit(newText)
        })
    }
}
