package com.heppihome.ui.tasks_view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class TaskView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {  }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun Tasks() {
    Column(modifier = Modifier.fillMaxWidth()) {
        topbar()
        for (i in 1..5) {
            day(i)
        }
    }
}

@Composable
fun topbar(){
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Return", modifier = Modifier.size(40.dp))
            }
            Row(modifier = Modifier.padding(10.dp)) {
                Text("Group X", fontSize = 30.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End) {
                Icon(Icons.Default.MoreVert, contentDescription = "Options", modifier = Modifier.size(40.dp))
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun day(day: Int){
    var expended by remember { mutableStateOf(false)}
    val rotation by animateFloatAsState(targetValue = if(expended) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = MaterialTheme.shapes.medium,
        onClick = {
            expended = !expended
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ){
            Row{
                Text(
                    modifier = Modifier.weight(6f),
                    text = "Day $day",
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight =  FontWeight.Bold
                )
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(1f)
                        .rotate(rotation),
                    onClick = {
                        expended = !expended
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "DropDown Arrow"
                    )
                }
            }
            if(expended){
                Row{
                    Checkbox(checked = true, onCheckedChange = {})
                    Text(text = "Task 1",
                        fontSize = MaterialTheme.typography.subtitle1.fontSize)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), horizontalArrangement = Arrangement.End) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", modifier = Modifier.size(20.dp))
                    }
                }
                Row{
                    Checkbox(checked = false, onCheckedChange = {})
                    Text(text = "Task 2",
                        fontSize = MaterialTheme.typography.subtitle1.fontSize)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), horizontalArrangement = Arrangement.End) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

