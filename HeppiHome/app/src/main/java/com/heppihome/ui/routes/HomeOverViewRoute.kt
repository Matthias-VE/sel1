package com.heppihome.ui.routes

import android.widget.CalendarView
import androidx.compose.runtime.Composable
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Calendar
import com.heppihome.viewmodels.HomeOverviewViewModel
import java.util.*

@Composable
fun HomeOverViewRoute(
    vM : HomeOverviewViewModel,
    onButtonClicked : (GregorianCalendar) -> Unit
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