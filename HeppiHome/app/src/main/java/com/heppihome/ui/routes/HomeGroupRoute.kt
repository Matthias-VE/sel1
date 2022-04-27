package com.heppihome.ui.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.data.models.Group
import com.heppihome.viewmodels.HomeGroupViewModel

@Composable
fun HomeGroupRoute(
    vM : HomeGroupViewModel,
    onGroupClicked : (Group) -> Unit
) {
    vM.refreshGroups()
    val groups by vM.groups.collectAsState()
    val expanded by vM.expanded.collectAsState()

    HomeGroupScreen(
        groups,
        onGroupClicked,
        expanded,
        { vM.expandGroupMenu() }
    )
}

@Composable
fun HomeGroupScreen(
    groups: List<Group>,
    onGroupClicked: (Group) -> Unit,
    expanded: Boolean,
    toggle: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Header(expanded, toggle)
        Alltasks()
        Groups(groups, onGroupClicked)
    }
}

@Composable
fun Header(expanded: Boolean, toggle: () -> Unit) {
    
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp)) {
                Text("Groups", fontSize = 30.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End) {
                DropdownIcon(expanded = expanded, toggle = toggle)
            }
        }
    }
}

@Composable
fun Alltasks() {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
            Text("All tasks", fontSize = 30.sp)
        }
    }
}

@Composable
fun Groups(groups : List<Group>, onGroupClicked: (Group) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(groups) { group ->
            Groupdetail(group, onGroupClicked)
        }
    }

}

@Composable
fun Groupdetail(g : Group, onGroupClicked: (Group) -> Unit) {
    Button(onClick = {onGroupClicked(g)}) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, Color.Gray))
                .padding(20.dp)
        ) {
            Text(text = g.name, fontSize = 30.sp)
            Text(text = g.description, fontSize = 20.sp, color = Color.Gray)
        }
    }
}

@Composable
fun DropDown(expanded: Boolean, toggle: () -> Unit) {

    DropdownMenu(expanded = expanded, onDismissRequest = toggle) {
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

@Composable
fun DropdownIcon(expanded: Boolean, toggle: () -> Unit) {

    IconButton(onClick = toggle) {
        Icon(
            Icons.Default.MoreVert, contentDescription = "Options", modifier = Modifier
                .size(40.dp)
        )
        DropDown(expanded = expanded, toggle = toggle)
    }
}
