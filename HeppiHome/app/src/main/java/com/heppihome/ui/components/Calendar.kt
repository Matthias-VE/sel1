package com.heppihome.ui.components

import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.heppihome.R

@Preview
@Composable
fun cal(){
    var date by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = { TopAppBar(title = {Text( text = stringResource(R.string.Calendar))})},
        content = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                AndroidView(factory = { CalendarView(it)},
                    update = { it.setOnDateChangeListener { _, year, month, day ->
                        date = "$day - ${month + 1} - $year"
                    }
                })
                Text(text = "zie taken op $date")
            }
        }
    )
}