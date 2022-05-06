package com.heppihome.ui.routes.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.ui.components.Header
import com.heppihome.ui.components.InputField
import com.heppihome.viewmodels.tasks.AddTaskViewModel
import java.util.*

@Composable
fun AddTaskRoute(
    vM : AddTaskViewModel = hiltViewModel(),
    group : Group,
    onCancelled: () -> Unit
) {
    vM.usersInGroup(group)
    val name by vM.name.collectAsState()
    val users by vM.users.collectAsState()
    val usersInGroup by vM.usersInGroup.collectAsState()
    val date by vM.date.collectAsState()
    val hours by vM.hours.collectAsState()

    AddTaskScreen(
        name,
        vM.calendar,
        users,
        usersInGroup,
        vM::checkUser,
        onCancelled,
        {vM.updateName(it)},
        {vM.addTask(group); onCancelled()},
        hours, date,
        vM::updateDate,
        vM::updateHours
    )
}
@Composable
fun AddTaskScreen(
    name : String,
    cal : Calendar,
    users: List<String>,
    usersInGroup : List<User>,
    onCheckUser: (User, Boolean) -> Unit,
    onCancelled : () -> Unit,
    onNameChanged : (String) -> Unit,
    onSaveTask : () -> Unit,
    hours: String, date : String,
    updateDatepicker: (Int, Int, Int) -> Unit,
    updateTimePicker: (Int, Int) -> Unit
) {
    Column() {
        Header("Add Task", onCancelled)
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
                InputField(name = "Task", description = name, onNameChanged)
                UserSelection(users = usersInGroup, users, onCheckUser = onCheckUser)
                Log.i("AddTaskRoute", "users size: " + users.size)
                CalendarView(cal,
                   hours, date,
                    updateDatepicker, updateTimePicker)
                Button(onClick = onSaveTask , enabled = users.isNotEmpty(), modifier = Modifier.padding(10.dp)) {
                    Text("Add")
                }

        }
    }

}

@Composable
fun UserSelection(users : List<User>, selected : List<String>, onCheckUser : (User, Boolean) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        Text("This task is for:", fontSize = MaterialTheme.typography.body1.fontSize)
        for (user in users) {
            var checked by remember { mutableStateOf(user.id in selected)}
            Row {
                Checkbox(checked = checked, onCheckedChange = {checked = it; onCheckUser(user, it)})
                Text(
                    text = user.name,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                )

            }
        }
    }
}

@Composable
fun CalendarView(cal : Calendar, hours : String, date : String,
                 updateDatepicker : (Int, Int, Int) -> Unit,
                 updateTimePicker: (Int, Int) -> Unit) {

    val mContext = LocalContext.current


    val mYear = cal.get(Calendar.YEAR)
    val mMonth = cal.get(Calendar.MONTH)
    val mDay = cal.get(Calendar.DAY_OF_MONTH)


    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, fYear: Int, fMonth: Int, fDayOfMonth: Int ->
            updateDatepicker(fDayOfMonth, fMonth, fYear)
        }, mYear, mMonth, mDay
    )

    val mHour = 0
    val mMinute = 0


    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, fHour : Int, fMinute: Int ->
            updateTimePicker(fHour, fMinute)
        }, mHour, mMinute, true
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {

        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary) ) {
            Text(text = "Open Date Picker", color = Color.White)
        }

        Text(text = "Selected Date: $date", textAlign = TextAlign.Center)

        Button(onClick = {
            mTimePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary) ) {
            Text(text = "Open Time Picker", color = Color.White)
        }

        Text(text = "Selected Time: $hours", textAlign = TextAlign.Center)
    }

}
