package com.heppihome.ui.navigation

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route:String){

    object Tasks: BottomNavItem("Tasks", android.R.drawable.ic_menu_edit, HomeAppDestinations.TASKS_ROUTE)
    object Overview: BottomNavItem("Overview", android.R.drawable.ic_menu_my_calendar, "calendar")
    object Shop : BottomNavItem("Shop", 666, HomeAppDestinations.SHOP_ROUTE)
    object Settings: BottomNavItem("Settings", android.R.drawable.ic_menu_preferences, "settings")
}