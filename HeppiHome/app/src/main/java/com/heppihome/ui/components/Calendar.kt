package com.heppihome.ui.components

import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.HomeOverviewViewModel
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Calendar(
             vM: HomeOverviewViewModel,
             onDateChange : (CalendarView, Int, Int, Int) -> Unit,
             format : SimpleDateFormat
) {
    vM.resetDate()
    val date by vM.date.collectAsState()
    val tasks by vM.tasks.collectAsState()
    Scaffold(
        topBar = { TopbarNoBackArrow(title = stringResource(R.string.Calendar)) },
        content = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                AndroidView(
                    factory = { CalendarView(it)},
                    update = {
                        it.setOnDateChangeListener(onDateChange)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = {} ) {
                    Text(text = "view tasks on $date")
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()){
                    items(1){
                        if(tasks.isEmpty()){
                            Text(text = "No tasks due on $date")
                        }else {
                            for (task in tasks) {
                                Row {
                                    Checkbox(
                                        checked = task.done,
                                        onCheckedChange = { vM.toggleTask(task) })
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
    )
}
