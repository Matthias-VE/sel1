package com.heppihome.ui.routes.tasks

import com.heppihome.viewmodels.tasks.EditTaskViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.ui.components.Header
import com.heppihome.ui.components.InputField
import com.heppihome.ui.components.InputNumberField
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.tasks.AddTaskViewModel
import java.lang.Integer.parseInt
import java.util.*

@Composable
fun EditTaskRoute(
    vM : EditTaskViewModel = hiltViewModel(),
    onCancelled: (Task) -> Unit,
    task : Task
) {

    LaunchedEffect(Unit) {
        vM.setTask(task)
    }
    vM.usersInGroup()

    val isAdmin by vM.isAdmin.collectAsState()
    val name by vM.name.collectAsState()
    val points by vM.points.collectAsState()
    val users by vM.users.collectAsState()
    val usersInGroup by vM.usersInGroup.collectAsState()
    val date by vM.date.collectAsState()
    val hours by vM.hours.collectAsState()

    EditTaskScreen(
        name,
        isAdmin,
        points,
        vM.calendar,
        users,
        usersInGroup,
        vM::checkUser,
        {onCancelled(vM.getTask())},
        {vM.updateName(it)},
        {vM.updateTask(); onCancelled(vM.getTask())},
        hours, date,
        vM::updateDate,
        vM::updateHours,
        vM::updatePoints
    )
}


@Composable
fun EditTaskScreen(
    name : String,
    isAdmin : Boolean,
    points : String,
    cal : Calendar,
    users: List<String>,
    usersInGroup : List<User>,
    onCheckUser: (User, Boolean) -> Unit,
    onCancelled : () -> Unit,
    onNameChanged : (String) -> Unit,
    onSaveTask : () -> Unit,
    hours: String, date : String,
    updateDatepicker: (Int, Int, Int) -> Unit,
    updateTimePicker: (Int, Int) -> Unit,
    updatePoints: (String) -> Unit
) {
    Column() {
        Topbar(stringResource(id = R.string.AddTask), onCancelled)
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            InputField(name = stringResource(R.string.Task), description = name, onNameChanged)
            if (isAdmin) {
                InputNumberField(name = stringResource(R.string.Points), value = points, edit = updatePoints)
            }
            UserSelection(users = usersInGroup, users, onCheckUser = onCheckUser)
            Log.i("AddTaskRoute", "users size: " + users.size)
            CalendarView(cal,
                hours, date,
                updateDatepicker, updateTimePicker)
            Button(onClick = onSaveTask , enabled = users.isNotEmpty() && name.isNotBlank(), modifier = Modifier.padding(10.dp)) {
                Text(stringResource(R.string.Edit))
            }

        }
    }

}
