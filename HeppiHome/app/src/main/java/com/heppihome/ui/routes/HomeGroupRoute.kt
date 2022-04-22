package com.heppihome.ui.routes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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

    HomeGroupScreen(
        groups,
        onGroupClicked
    )
}


@Composable
fun HomeGroupScreen(
    groups : List<Group>,
    onGroupClicked: (Group) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Header()
        Alltasks()
        Groups(groups, onGroupClicked)
    }
}

@Composable
fun Header() {
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
                Icon(Icons.Default.MoreVert, contentDescription = "Options", modifier = Modifier.size(40.dp))
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
            modifier = Modifier.fillMaxWidth().border(BorderStroke(2.dp, Color.Gray)).padding(20.dp)
        ) {
            Text(text = g.name, fontSize = 30.sp)
            Text(text = g.description, fontSize = 20.sp, color = Color.Gray)
        }
    }
}
