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
    vM : HomeOverviewViewModel,
    onButtonClicked : (GregorianCalendar) -> Unit,
) {

    HomeOverViewScreen(
        vM,
        vM::onDateChange,
        onButtonClicked
    )
}

@Composable
fun HomeOverViewScreen(
    vM: HomeOverviewViewModel,
    onDateChange: (CalendarView, Int, Int, Int) -> Unit,
    onButtonClicked : (GregorianCalendar) -> Unit
) {
    Calendar(vM, onDateChange, onButtonClicked)
}