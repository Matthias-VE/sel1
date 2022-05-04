package com.heppihome.ui.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.heppihome.data.models.User
import com.heppihome.ui.components.Topbar

@Composable
fun HomeProfileRoute() {

}

@Composable
fun HomeProfileScreen(user : User) {
    Column {
        Topbar(title = "Profile") {

        }
    }
}

@Preview
@Composable
fun HomeProfileRoutePreview() {
    HomeProfileRoute()
}