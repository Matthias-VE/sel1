package com.heppihome.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.R
import com.heppihome.viewmodels.groups.AddGroupViewModel

@Composable
fun NewGroup(
    vM : AddGroupViewModel,
    onGroupCancel : () -> Unit
) {
    val temp by vM.groupName.collectAsState()
    val temp2 by vM.description.collectAsState()

    Column() {
        Header("New Group", onGroupCancel)
        Column(modifier = Modifier
            .padding(10.dp)) {
            InputField(name = stringResource(R.string.GroupName), description = temp.text, { x -> vM.setGroup(x)})
            InputField(name = stringResource(R.string.Description), description = temp2.text, { x -> vM.setDescription(x)})
            Button(onClick = { vM.addGroups() },
                modifier = Modifier.padding(10.dp)) {
                Text(stringResource(R.string.Add))
            }
        }
    }

}

@Composable
fun Header(title : String, onGroupCancel: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row() {
            Row(modifier = Modifier
                .padding(10.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = onGroupCancel) {
                    Icon(
                        Icons.Default.Close, contentDescription = stringResource(R.string.Cancel), modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
                Text(title, fontSize = 30.sp, textAlign = TextAlign.Center)
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
