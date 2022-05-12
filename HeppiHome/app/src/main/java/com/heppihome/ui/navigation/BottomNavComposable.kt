package com.heppihome.ui.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Tasks,
        BottomNavItem.Overview,
        BottomNavItem.Shop
    )

    BottomNavigation(
        backgroundColor = Color.Blue,
        contentColor = Color.LightGray
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->

            BottomNavigationItem(
                icon = { Icon(item.icon, item.title) },
                label = { Text(text = item.title, fontSize = 14.sp) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.LightGray,
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let {screen_route:String ->
                            popUpTo(screen_route) {saveState = true}
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
