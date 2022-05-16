package com.heppihome.ui.components

import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.HomeOverviewViewModel
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Calendar(
             onDateChange : (CalendarView, Int, Int, Int) -> Unit,
             tasks : List<Task>,
             date : String
) {


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
                Button(onClick = {} ) {
                    Text(text = "view tasks on $date")
                }

                //om te testen
                LazyColumn(modifier = Modifier.fillMaxWidth()){
                    items(tasks){
                        Text(text = "Task: ${it.text}")
                    }
                }
            }
        }
    )
}
