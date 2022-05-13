package com.heppihome.ui.components

import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.HomeOverviewViewModel
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Calendar(date: String,
             vM: HomeOverviewViewModel,
             onDateChange : (CalendarView, Int, Int, Int) -> Unit,
             format : SimpleDateFormat,
             onBackPressed : () -> Unit
) {
    vM.refreshGroups()
    val groups by vM.groups.collectAsState()
    var groupsWithTasks : MutableMap<Group, List<Task>> = mutableMapOf()
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
                        groupsWithTasks = vM.getGroupsWithTasks(groups, vM.cal)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()){
                    items(1){
                        Text(text = "view tasks on $date")
                        Text(text = "groupsWithTasks : $groupsWithTasks")
                        Text(text= "$groups")
                    }
                }
            }
        }
    )
}
