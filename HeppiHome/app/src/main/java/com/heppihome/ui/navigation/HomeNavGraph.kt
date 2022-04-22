package com.heppihome.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.heppihome.ui.routes.HomeGroupRoute
import com.heppihome.ui.routes.HomeOverViewRoute
import com.heppihome.ui.routes.HomeSettingsRoute
import com.heppihome.ui.routes.HomeTasksRoute

@Composable
fun HomeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination : String = HomeAppDestinations.GROUP_ROUTE

){
    NavHost(navController = navController, startDestination = startDestination) {
        composable(HomeAppDestinations.GROUP_ROUTE) {
            HomeGroupRoute(vM = hiltViewModel(), onGroupClicked = {
                navController.navigate(HomeAppDestinations.TASKS_ROUTE + "/${it.id}")
            })
        }

        composable(BottomNavItem.Overview.screen_route) {
            HomeOverViewRoute()
        }

        composable(route = BottomNavItem.Tasks.screen_route) {
            var id = ""
            it.arguments?.getString("groupId")?.let { it1 ->
                id = it1
            }
            HomeTasksRoute(vM = hiltViewModel(), onBackPressed = {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            }, groupId = id)
        }

        composable(BottomNavItem.Settings.screen_route){
            HomeSettingsRoute()
        }
    }
}