package com.heppihome.ui.routes

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Tasks
import com.heppihome.viewmodels.HomeTasksViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTasksRoute(
    vM : HomeTasksViewModel,
    onBackPressed : () -> Unit,
    groupId : String
){
    val g = vM.getGroup(groupId)
    vM.onChangeGroup(g)
    val tasks by vM.tasks.collectAsState()
    val list = listOf(tasks, emptyList<Task>(), emptyList<Task>(), emptyList<Task>(), emptyList<Task>())
    HomeTasksScreen(allTasks = list, {vM.toggleTask(it)}, vM.group, {vM.onGoBack(); onBackPressed()})
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTasksScreen(
    allTasks : List<List<Task>>,
    onChecked : (Task) -> Unit,
    group : Group,
    onBackPressed: () -> Unit
    ) {
    Tasks(allTasks = allTasks, onChecked = onChecked, group = group, onBackPressed)
}