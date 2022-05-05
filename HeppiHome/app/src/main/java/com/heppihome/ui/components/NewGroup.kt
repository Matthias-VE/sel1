package com.heppihome.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.R
import com.heppihome.ui.routes.DropdownIcon
import com.heppihome.viewmodels.AddGroupViewModel

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
            InputField(name = stringResource(R.string.GroupName), description = temp.text, vM, { x -> vM.setGroup(x)})
            InputField(name = stringResource(R.string.Description), description = temp2.text, vM, { x -> vM.setDescription(x)})
            Button(onClick = { vM.addGroups() },
                modifier = Modifier.padding(10.dp)) {
                Text(stringResource(R.string.Add))
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
                        Icons.Default.Close, contentDescription = stringResource(R.string.Cancel), modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
                Text(stringResource(R.string.NewGroup), fontSize = 30.sp, textAlign = TextAlign.Center)
            }

        }
    }
}

@Composable
fun InputField(name : String, description : String, vM: AddGroupViewModel, edit : (String) -> Unit) {

    Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
        Text(name, color = Color.Gray)
        OutlinedTextField(value = description, modifier = Modifier.fillMaxWidth(), onValueChange = { newText ->
            edit(newText)
        })
    }
}
