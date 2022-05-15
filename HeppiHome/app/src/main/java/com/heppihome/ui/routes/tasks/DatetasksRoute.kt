package com.heppihome.ui.routes.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.tasks.DateTasksViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DateTasksRoute(
    vM : DateTasksViewModel,
    onBackPressed : () -> Unit,
    calendar: GregorianCalendar
) {
    vM.setCalendar(calendar)
    vM.getTasks()
    val date by vM.date.collectAsState()
    val tasks by vM.tasks.collectAsState()
    val format = SimpleDateFormat("kk:mm", Locale.getDefault())

    Column {
        Topbar(date,
            onBackPressed = onBackPressed
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(1) {
                if (tasks.isEmpty()) {
                    Text(text = "No tasks due on $date")
                } else {
                    for (task in tasks) {
                        Row {
                            Checkbox(
                                checked = task.done,
                                onCheckedChange = {
                                    vM.toggleTask(task)
                                    vM.getTasks()
                                })
                            Text(
                                text = task.text,
                                fontSize = MaterialTheme.typography.subtitle1.fontSize
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.End
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

@Composable
fun DisplayTasks(tasks : List<Task>, date : String, toggleTask : (Task) -> Unit, getTasks : () -> Unit, format : SimpleDateFormat){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(1) {
            for (task in tasks) {
                Row {
                    Checkbox(
                        checked = task.done,
                        onCheckedChange = {
                            toggleTask(task)
                            getTasks()
                        })
                    Text(
                        text = task.text,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.End
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