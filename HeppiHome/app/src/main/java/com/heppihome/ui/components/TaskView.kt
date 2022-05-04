package com.heppihome.ui.components

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
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task


//Task View
@ExperimentalMaterialApi
@Composable
fun Tasks(tasksToday: List<Task>, tasksTomorrow: List<Task>, onChecked: (Task) -> Unit, group : Group, onBackPressed : () -> Unit) {
    Column {
        Topbar(group.name, onBackPressed)
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(1) {
                Day("today", tasksToday, onChecked)
                Day("tomorrow", tasksTomorrow, onChecked)
            }
        }
    }
}

@Composable
fun Topbar(g : Group, onBackPressed: () -> Unit){
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { onBackPressed()}) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Return", modifier = Modifier.size(40.dp))
                }
            }
            Row(modifier = Modifier.padding(10.dp)) {
                Text(g.name, fontSize = 30.sp)
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
fun Day(day : String, tasks : List<Task>, onChecked : (Task) -> Unit){
    var expended by remember { mutableStateOf(true)}
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
                    text = day,
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
                for (task in tasks) {
                    Row {
                        Checkbox(checked = task.done, onCheckedChange = {onChecked(task)})
                        Text(
                            text = task.text,
                            fontSize = MaterialTheme.typography.subtitle1.fontSize
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp), horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Options",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}


