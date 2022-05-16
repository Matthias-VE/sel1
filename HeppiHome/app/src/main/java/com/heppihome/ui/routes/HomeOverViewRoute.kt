package com.heppihome.ui.routes

import android.widget.CalendarView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Calendar
import com.heppihome.viewmodels.HomeOverviewViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeOverViewRoute(
    vM : HomeOverviewViewModel = hiltViewModel()
) {

    val tasks by vM.tasks.collectAsState()

    val date by vM.date.collectAsState()

    HomeOverViewScreen(
        vM::onDateChange,
        tasks,
        date
    )
}

@Composable
fun HomeOverViewScreen(
    onDateChange: (CalendarView, Int, Int, Int) -> Unit,
    tasks : List<Task>,
    date : String
) {
    val format = SimpleDateFormat("kk:mm", Locale.getDefault())
    Calendar(onDateChange, tasks, date)
}