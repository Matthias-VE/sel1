package com.heppihome.ui.routes.tasks

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.tasks.AllTasksViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllTasksRoute(
    vM : AllTasksViewModel,
    onBackPressed : () -> Unit ,
){
    vM.getGroupsWithTasks()
    val groupsWithTasks by vM.groupsWithTasks.collectAsState()
    val date by vM.date.collectAsState()
    val format = SimpleDateFormat("kk:mm", Locale.getDefault())

    Column {
        Topbar(date,
            onBackPressed = onBackPressed
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(1) {
                for((group, tasks) in groupsWithTasks){
                    GroupWithTasks(group = group, tasks = tasks, onChecked = vM::toggleTask, format = format)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun GroupWithTasks(group: Group, tasks : List<Task>, onChecked : (Task, Group) -> Unit, format : SimpleDateFormat){

    var expended by remember { mutableStateOf(true) }
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

        ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ){
            Row(modifier = Modifier.clickable {
                expended = !expended
            }){
                Text(
                    modifier = Modifier.weight(6f),
                    text = group.name,
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
                if(tasks.isEmpty()){
                    Text(text = "No tasks")
                } else {
                    for (task in tasks) {
                        Row {
                            Checkbox(
                                checked = task.done,
                                onCheckedChange = { onChecked(task, group) })
                            Text(
                                text = task.text,
                                fontSize = MaterialTheme.typography.subtitle1.fontSize
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp), horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = format.format(task.deadline.toDate())
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}