package com.heppihome.ui.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.HomeSettingsViewModel

@Composable
fun HomeSettingsRoute(
    onBackPressed : () -> Unit,
    onProfileClicked : () -> Unit
) {

    val vM : HomeSettingsViewModel = hiltViewModel()
    HomeSettingsScreen(onBackPressed, onProfileClicked)
}

@Composable
fun HomeSettingsScreen(onBackPressed: () -> Unit, onProfileClicked: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Topbar(stringResource(R.string.Settings), onBackPressed)
        AllSettings(onProfileClicked = onProfileClicked)
    }
}


@Composable
fun AllSettings(onProfileClicked: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(1) { index ->
            Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable(onClick = onProfileClicked)
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp)
                    )
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

