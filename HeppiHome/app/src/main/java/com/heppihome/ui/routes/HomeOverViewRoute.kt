package com.heppihome.ui.routes

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.ui.components.Calendar
import com.heppihome.viewmodels.HomeOverviewViewModel

@Composable
fun HomeOverViewRoute(
    vM : HomeOverviewViewModel = hiltViewModel()
) {

    HomeOverViewScreen()
}

@Composable
fun HomeOverViewScreen() {
    Calendar()
}