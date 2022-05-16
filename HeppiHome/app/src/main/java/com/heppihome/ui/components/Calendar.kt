package com.heppihome.ui.components

import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.heppihome.R
import com.heppihome.viewmodels.HomeOverviewViewModel
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Calendar(
             vM: HomeOverviewViewModel,
             onDateChange : (CalendarView, Int, Int, Int) -> Unit,
             onButtonClicked : (GregorianCalendar) -> Unit
) {
    vM.resetDate()
    val date by vM.date.collectAsState()

    Scaffold(
        topBar = { TopbarNoBackArrow(title = stringResource(R.string.Calendar)) },
        content = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                AndroidView(
                    factory = { CalendarView(it)},
                    update = {
                        it.setOnDateChangeListener(onDateChange)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = {
                    onButtonClicked(vM.cal)
                } ) {
                    Text(text = stringResource(R.string.TasksOnDate) + date)
                }
            }
        }
    )
}
