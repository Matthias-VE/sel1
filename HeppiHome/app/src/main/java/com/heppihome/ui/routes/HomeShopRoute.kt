package com.heppihome.ui.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.data.models.Group
import com.heppihome.ui.components.TopbarNoBackArrow
import com.heppihome.viewmodels.HomeShopViewModel

@Composable
fun HomeShopRoute(
    vM : HomeShopViewModel = hiltViewModel(),
    g : Group
){
    vM.setGroup(g)

    val points by vM.points.collectAsState()
    HomeShopScreen(points = points.points)
}

@Composable
fun HomeShopScreen(points : Int) {
    Column {
        TopbarNoBackArrow(title = "Shop")
        Text("You currently have $points points")
    }

}