package com.heppihome.ui.routes.tasks

import android.content.Context
import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import com.heppihome.R
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
    onInvitePerson: () -> Unit,
    onMakeSomeoneAdmin: () -> Unit
){

    vM.startListeners()

    val context = LocalContext.current
    val txt = stringResource(R.string.CantResignAdmin)

    val tasksToday by vM.tasksToday.collectAsState()
    val tasksTomorrow by vM.tasksTomorrow.collectAsState()
    val expanded by vM.expanded.collectAsState()
    val isAdmin by vM.isAdmin.collectAsState()


    HomeTasksScreen(tasksToday, tasksTomorrow,expanded,vM::toggleDropdownMenu, onAddTask ,
        {vM.toggleTask(it)}, vM.group(),
        {vM.onGoBack(); onBackPressed()},
        onInvitePerson, isAdmin,
        {
            // Als resign failed
            if (!vM.resignAsAdmin()) {
                Toast.makeText(context, txt, Toast.LENGTH_LONG).show()
            }
        }, onMakeSomeoneAdmin
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
    onInvitePerson : () -> Unit,
    isAdmin : Boolean,
    onResignAsAdmin : () -> Unit,
    onMakeSomeoneAdmin : () -> Unit
    ) {
    val format = SimpleDateFormat("kk:mm", java.util.Locale.getDefault())
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick =  onAddTask ) {
            Icon(Icons.Default.Add, "add task button")
        }
    }, floatingActionButtonPosition = FabPosition.End) {
        Tasks(today, tomorrow,expanded, toggleDropDown, onChecked = onChecked,
            group = group, onBackPressed, onInvitePerson, format,
            isAdmin, onResignAsAdmin, onMakeSomeoneAdmin
        )
    }
}