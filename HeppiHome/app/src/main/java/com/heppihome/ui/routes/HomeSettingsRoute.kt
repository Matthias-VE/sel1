package com.heppihome.ui.routes

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.viewmodels.HomeSettingsViewModel

@Composable
fun HomeSettingsRoute() {

    val vM : HomeSettingsViewModel = hiltViewModel()

    HomeSettingsScreen()
}

@Preview(showBackground = true)
@Composable
fun HomeSettingsScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        SettingsHeader()
        AllSettings()
    }
}

@Composable
fun SettingsHeader() {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp)) {
                Text(stringResource(R.string.Settings), fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun AllSettings() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(1) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile", modifier = Modifier.size(40.dp))
                Text(text = stringResource(R.string.Profile), modifier = Modifier.padding(4.dp), fontSize = 25.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", modifier = Modifier.size(40.dp))
                Text(text = stringResource(R.string.Notifications), modifier = Modifier.padding(4.dp), fontSize = 25.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Language", modifier = Modifier.size(40.dp))
                Text(text = stringResource(R.string.Language), modifier = Modifier.padding(4.dp), fontSize = 25.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Help", modifier = Modifier.size(40.dp))
                Text(text = stringResource(R.string.Help), modifier = Modifier.padding(4.dp), fontSize = 25.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Theme", modifier = Modifier.size(40.dp))
                Text(text = stringResource(R.string.Theme), modifier = Modifier.padding(4.dp), fontSize = 25.sp)
            }
        }
    }
}

