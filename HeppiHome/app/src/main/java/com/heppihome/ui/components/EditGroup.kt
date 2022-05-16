package com.heppihome.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.viewmodels.groups.EditGroupViewModel

@Composable
fun EditGroup(
    vM: EditGroupViewModel,
    onGroupCancel: () -> Unit,
    g: Group
) {
    //vM.setGroup(g)
    val groupName by vM.groupName.collectAsState()
    val description by vM.description.collectAsState()


    // Raise Toast on state of status
    val status by vM.status.collectAsState()
    
    LaunchedEffect(Unit) {
        vM.setName(g.name)
        vM.setDescription(g.description)
    }

    val context = LocalContext.current

    Column() {
        EditGroupHeader(onGroupCancel = onGroupCancel)
        Column(modifier = Modifier
            .padding(10.dp)) {
            InputField(name = stringResource(R.string.GroupName), description = groupName.text) { x -> vM.setName(x) }
            InputField(name = stringResource(R.string.Description), description = description.text) { x -> vM.setDescription(x) }
            Button(onClick = {
                if (vM.isValid(context)) {
                    vM.editGroup(g, context);

                    onGroupCancel();
                }
                when (status) {
                    is ResultState.Failed -> "Toastje me zalm"
                    is ResultState.Loading -> "Toastje me choco"
                    is ResultState.Success -> "Toastje me wreed goei beleg"
                    else -> Unit
                }

            },
                modifier = Modifier.padding(10.dp)) {
                Text(stringResource(R.string.Edit))
            }
        }
    }
}

@Composable
fun EditGroupHeader(onGroupCancel: () -> Unit) {
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
                Text(stringResource(R.string.EditGroup), fontSize = 30.sp, textAlign = TextAlign.Center)
            }

        }
    }
}
