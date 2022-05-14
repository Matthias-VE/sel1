package com.heppihome.ui.routes.tasks

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.intl.Locale
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Tasks
import com.heppihome.viewmodels.tasks.HomeTasksViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTasksRoute(
    vM : HomeTasksViewModel,
    onAddTask : () -> Unit,
    onBackPressed : () -> Unit,
    onInvitePerson: () -> Unit
){

    vM.startListeners()

    val tasksToday by vM.tasksToday.collectAsState()
    val tasksTomorrow by vM.tasksTomorrow.collectAsState()
    val expanded by vM.expanded.collectAsState()

    HomeTasksScreen(tasksToday, tasksTomorrow,expanded,vM::toggleDropdownMenu, onAddTask ,
        {vM.toggleTask(it)}, vM.group(),
        {vM.onGoBack(); onBackPressed()},
        onInvitePerson
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTasksScreen(
    today : List<Task>,
    tomorrow : List<Task>,
    expanded : Boolean,
    toggleDropDown : () -> Unit,
    onAddTask: () -> Unit,
    onChecked : (Task) -> Unit,
    group : Group,
    onBackPressed: () -> Unit,
    onInvitePerson : () -> Unit
    ) {
    val format = SimpleDateFormat("kk:mm", java.util.Locale.getDefault())
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick =  onAddTask ) {
            Icon(Icons.Default.Add, "add task button")
        }
    }, floatingActionButtonPosition = FabPosition.End) {
        Tasks(today, tomorrow,expanded, toggleDropDown, onChecked = onChecked,
            group = group, onBackPressed, onInvitePerson, format)
    }
}