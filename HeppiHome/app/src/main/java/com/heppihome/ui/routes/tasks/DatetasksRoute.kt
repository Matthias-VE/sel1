package com.heppihome.ui.routes.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.tasks.DateTasksViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DateTasksRoute(
    vM : DateTasksViewModel,
    onBackPressed : () -> Unit,
    calendar: GregorianCalendar,
    onTaskPressed : (Task) -> Unit
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
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.padding(5.dp))
                Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
                    Text(stringResource(R.string.Tasks), fontSize = 30.sp)
                }
            }
        }
        if (tasks.isEmpty()) {
            Column(
                modifier =  Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = stringResource(R.string.NoTasksDue)  + date)
                }
            }
        } else {
            LazyColumn(
                modifier =  Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(1) {
                    Spacer(Modifier.padding(10.dp))
                    for (task in tasks) {
                        Row(Modifier.clickable(true, onClick = {onTaskPressed(task)})) {
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