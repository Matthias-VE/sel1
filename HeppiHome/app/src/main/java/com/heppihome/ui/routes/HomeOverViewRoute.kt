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
    vM : HomeOverviewViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val date by vM.date.collectAsState()
    HomeOverViewScreen(
        date,
        onBackPressed,
        vM,
        vM::onDateChange
    )
}

@Composable
fun HomeOverViewScreen(
    date: String,
    onBackPressed: () -> Unit,
    vM: HomeOverviewViewModel,
    onDateChange: (CalendarView, Int, Int, Int) -> Unit
) {
    val format = SimpleDateFormat("kk:mm", Locale.getDefault())
    Calendar(date, vM, onDateChange, format,onBackPressed)
}