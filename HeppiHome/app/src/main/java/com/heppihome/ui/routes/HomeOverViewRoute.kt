package com.heppihome.ui.routes

import android.widget.CalendarView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.ui.components.Calendar
import com.heppihome.viewmodels.HomeOverviewViewModel

@Composable
fun HomeOverViewRoute(
    vM : HomeOverviewViewModel = hiltViewModel()
) {

    val date by vM.date.collectAsState()
    HomeOverViewScreen(
        date,
        vM::onDateChange
    )
}

@Composable
fun HomeOverViewScreen(
    date : String,
    onDateChange : (CalendarView, Int, Int, Int) -> Unit
) {
    Calendar(date, onDateChange)
}