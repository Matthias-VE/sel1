package com.heppihome.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var screen_route:String){

    object Tasks: BottomNavItem("Tasks", Icons.Filled.Edit, HomeAppDestinations.TASKS_ROUTE)
    object Overview: BottomNavItem("Overview", Icons.Filled.DateRange, HomeAppDestinations.CALENDAR)
    object Shop : BottomNavItem("Shop", Icons.Filled.ShoppingCart, HomeAppDestinations.SHOP_ROUTE)
    object Settings: BottomNavItem("Settings", Icons.Filled.Settings, HomeAppDestinations.SETTINGS_ROUTE)
}