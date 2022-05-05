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
    val deadline by vM.deadline.collectAsState()

    AddTaskScreen(
        name,
        deadline,
        users,
        usersInGroup,
        vM::checkUser,
        onCancelled,
        {vM.updateName(it)},
        {vM.addTask(group); onCancelled()}
    )
}
@Composable
fun AddTaskScreen(
    name : String,
    deadline : Timestamp,
    users: List<String>,
    usersInGroup : List<User>,
    onCheckUser: (User, Boolean) -> Unit,
    onCancelled : () -> Unit,
    onNameChanged : (String) -> Unit,
    onSaveTask : () -> Unit
) {
    Column() {
        Header("Add Task", onCancelled)
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
                InputField(name = "Task", description = name, onNameChanged)
                UserSelection(users = usersInGroup, users, onCheckUser = onCheckUser)
                Log.i("AddTaskRoute", "users size: " + users.size)
                Button(onClick = onSaveTask , enabled = users.isNotEmpty(),
                    modifier = Modifier.padding(10.dp)) {
                    Text("Add")
                }
        }
    }

}

@Composable
fun UserSelection(users : List<User>, selected : List<String>, onCheckUser : (User, Boolean) -> Unit) {
    Text("This task is for:", fontSize = MaterialTheme.typography.body1.fontSize, modifier = Modifier.padding(10.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
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
        Spacer(modifier = Modifier.size(50.dp))
        CalendarView()
    }
}

@Composable
fun CalendarView() {

    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    val mHour = 0
    val mMinute = 0

    // Value for storing time as a string
    val mTime = remember { mutableStateOf("") }

    // Creating a TimePicker dialod
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute"
        }, mHour, mMinute, false
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // Creating a button that on
        // click displays/shows the DatePickerDialog
        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {
            Text(text = "Open Date Picker", color = Color.White)
        }

        // Displaying the mDate value in the Text
        Text(text = "Selected Date: ${mDate.value}", fontSize = 30.sp, textAlign = TextAlign.Center)

        Button(onClick = {
            mTimePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {
            Text(text = "Open Time Picker", color = Color.White)
        }

        // Displaying the mDate value in the Text
        Text(text = "Selected Time: ${mTime.value}", fontSize = 30.sp, textAlign = TextAlign.Center)
    }

}

@Composable
fun HourPicker(){

    // Fetching local context
    val mContext = LocalContext.current

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    val mTime = remember { mutableStateOf("") }

    // Creating a TimePicker dialod
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        {_, mHour : Int, mMinute: Int ->
            mTime.value = "$mHour:$mMinute"
        }, mHour, mMinute, false
    )

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        // On button click, TimePicker is
        // displayed, user can select a time
        Button(onClick = { mTimePickerDialog.show() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))) {
            Text(text = "Open Time Picker", color = Color.White)
        }

        // Add a spacer of 100dp
        Spacer(modifier = Modifier.size(100.dp))

        // Display selected time
        Text(text = "Selected Time: ${mTime.value}", fontSize = 30.sp)
    }
}
