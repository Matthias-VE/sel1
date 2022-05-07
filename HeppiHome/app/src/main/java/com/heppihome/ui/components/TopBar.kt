package com.heppihome.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.R
import com.heppihome.data.models.Group

@Composable
fun Topbar(title : String, expanded: Boolean, toggle: () -> Unit, onBackPressed: () -> Unit, onInvitePerson: () -> Unit){
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick =  onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Return", modifier = Modifier.size(40.dp))
                }
            }
            Row(modifier = Modifier.padding(10.dp)) {
                Text(title, fontSize = 30.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = toggle) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Options",
                        modifier = Modifier.size(40.dp)
                    )
                    DropDownMenuTasks( expanded, toggle, onInvitePerson)
                }
            }
        }
    }
}

@Composable
fun DropDownMenuTasks(expanded : Boolean, toggle : () -> Unit, onInvitePerson : () -> Unit) {
    DropdownMenu(expanded = expanded, onDismissRequest = toggle) {
        DropdownMenuItem(onClick = onInvitePerson) {
            Text(stringResource(R.string.InviteToGroup))
        }

    }
}