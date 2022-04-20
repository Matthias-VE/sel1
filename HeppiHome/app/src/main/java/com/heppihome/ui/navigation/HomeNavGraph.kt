package com.heppihome.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.heppihome.ui.routes.HomeGroupRoute

@Composable
fun HomeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination : String = HomeAppDestinations.GROUP_ROUTE

){
    NavHost(navController = navController, startDestination = startDestination) {
        composable(HomeAppDestinations.GROUP_ROUTE) {
            HomeGroupRoute(vM = hiltViewModel())
        }

        composable(BottomNavItem.Overview.screen_route) {
            OverviewScreen()
        }

        composable(BottomNavItem.Tasks.screen_route) {
            TasksScreen()
        }

        composable(BottomNavItem.Settings.screen_route){
            SettingsScreen()
        }
    }
}