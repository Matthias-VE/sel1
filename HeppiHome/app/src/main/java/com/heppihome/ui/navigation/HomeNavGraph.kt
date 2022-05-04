package com.heppihome.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.heppihome.data.models.Constants

import com.heppihome.ui.components.NewGroup

import com.heppihome.data.models.Group
import com.heppihome.data.models.User
import com.heppihome.ui.routes.*
import com.heppihome.viewmodels.HomeMainViewModel

import com.heppihome.ui.routes.HomeGroupRoute
import com.heppihome.ui.routes.HomeOverViewRoute
import com.heppihome.ui.routes.HomeSettingsRoute
import com.heppihome.ui.routes.HomeTasksRoute

@Composable
fun HomeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination : String = HomeAppDestinations.LOGIN_ROUTE,
    vM : HomeMainViewModel
){


    NavHost(navController = navController, startDestination = startDestination) {
        composable(HomeAppDestinations.LOGIN_ROUTE) {
            HomeLoginRoute(vM = hiltViewModel(), onIsLoggedIn = {u ->
                vM.setUser(u)
            }, onIsLoggedInAndNavigateOnce = {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            }
            )
        }

        composable(HomeAppDestinations.GROUP_ROUTE) {
            HomeGroupRoute(vM = hiltViewModel(), onGroupClicked = {vM.selectedGroup = it;
                navController.navigate(HomeAppDestinations.TASKS_ROUTE) },
                onNewGroupClicked = {
                    navController.navigate(HomeAppDestinations.GROUP_ADD)
                }
            )
        }

        composable(HomeAppDestinations.GROUP_ADD) {
            NewGroup(vM = hiltViewModel(),
            onGroupCancel = {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            })
        }

        composable(BottomNavItem.Overview.screen_route) {
            HomeOverViewRoute()
        }

        composable(route = BottomNavItem.Tasks.screen_route) {

            HomeTasksRoute(vM = hiltViewModel(), onBackPressed = {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            }, group = vM.selectedGroup)
        }

        composable(BottomNavItem.Settings.screen_route){
            HomeSettingsRoute()
        }

        composable(HomeAppDestinations.PROFILE_ROUTE) {
            HomeProfileRoute()
        }
    }
}