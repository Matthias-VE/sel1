package com.heppihome.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.heppihome.R

sealed class BottomNavItem(var title: Int, var icon: ImageVector, var screen_route:String){

    object Tasks: BottomNavItem(R.string.Tasks, Icons.Filled.Edit, HomeAppDestinations.TASKS_ROUTE)
    object Overview: BottomNavItem(R.string.Overview, Icons.Filled.DateRange, HomeAppDestinations.CALENDAR)
    object Shop : BottomNavItem(R.string.Shop, Icons.Filled.ShoppingCart, HomeAppDestinations.SHOP_ROUTE)
    object Settings: BottomNavItem(R.string.Settings, Icons.Filled.Settings, HomeAppDestinations.SETTINGS_ROUTE)
}