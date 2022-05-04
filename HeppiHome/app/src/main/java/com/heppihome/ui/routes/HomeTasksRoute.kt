package com.heppihome.ui.routes

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.ui.components.Tasks
import com.heppihome.viewmodels.HomeTasksViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTasksRoute(
    vM : HomeTasksViewModel,
    onBackPressed : () -> Unit,
    group : Group?
){

    group?.let {
        vM.onChangeGroup(it)
    }
    val tasks by vM.tasks.collectAsState()
    HomeTasksScreen(allTasks = tasks, {vM.toggleTask(it)}, vM.group(), {vM.onGoBack(); onBackPressed()})
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTasksScreen(
    allTasks : List<Task>,
    onChecked : (Task) -> Unit,
    group : Group,
    onBackPressed: () -> Unit
    ) {
    Tasks(allTasks = allTasks, onChecked = onChecked, group = group, onBackPressed)
}