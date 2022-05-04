package com.heppihome.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.data.models.Group
import com.heppihome.viewmodels.groups.EditGroupViewModel

@Composable
fun EditGroup(
    vM: EditGroupViewModel,
    onGroupCancel: () -> Unit,
    g: Group
) {
    vM.setGroup(g)
    val temp by vM.groupName.collectAsState()
    val temp2 by vM.description.collectAsState()



    Column() {
        Header()
        Column(modifier = Modifier
            .padding(10.dp)) {
            InputField(name = "Name Of Group", description = temp.text) { x -> vM.setName(x) }
            InputField(name = "Description", description = temp2.text) { x -> vM.setDescription(x) }
            Button(onClick = { println("submitted") },
                modifier = Modifier.padding(10.dp)) {
                Text("Add")
            }
        }
    }

}

@Composable
fun Header() {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row() {
            Row(modifier = Modifier
                .padding(10.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { println("staph") }) {
                    Icon(
                        Icons.Default.Close, contentDescription = "Cancel", modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
                Text("Edit Group", fontSize = 30.sp, textAlign = TextAlign.Center)
            }

        }
    }
}
