package com.heppihome.ui.groupscreen

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.ui.navigation.BottomNavActivity
import com.heppihome.ui.navigation.MainScreenView

/*
class for graphical part of group selection scree
 */

class GroupScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Groupscreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Header()
        Alltasks()
        Groups()
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
fun Groups() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
       items(200) { index ->
           Groupdetail(name = "Item: $index", description = "werkt dit zo")
       }
    }
}

@Composable
fun Groupdetail(name: String, description: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .border(BorderStroke(2.dp, Color.Gray))
        .padding(20.dp)) {
        Text(text = "$name", fontSize = 30.sp)
        Text(text = "$description", fontSize = 20.sp, color = Color.Gray)
    }
}
