package com.heppihome.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.heppihome.ui.components.Calendar

import com.heppihome.ui.components.NewGroup

import com.heppihome.ui.components.EditGroup
import com.heppihome.ui.routes.*
import com.heppihome.ui.routes.groups.DetailInviteRoute
import com.heppihome.ui.routes.groups.HomeGroupRoute
import com.heppihome.ui.routes.groups.HomeInvitePersonRoute
import com.heppihome.ui.routes.groups.HomeInvitesRoute
import com.heppihome.ui.routes.tasks.AddTaskRoute
import com.heppihome.ui.routes.tasks.HomeTasksRoute
import com.heppihome.viewmodels.HomeMainViewModel

@Composable
fun HomeNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination : String = HomeAppDestinations.LOGIN_ROUTE,
    vM : HomeMainViewModel,
    context : Context
){


    NavHost(navController = navController, startDestination = startDestination) {

        composable(HomeAppDestinations.LOGIN_ROUTE) {
            HomeLoginRoute(vM = hiltViewModel(),
                ) {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            }
        }
        
        composable(HomeAppDestinations.GROUP_ROUTE) {
            HomeGroupRoute(vM = hiltViewModel(), onGroupClicked = {vM.selectedGroup = it;
                navController.navigate(HomeAppDestinations.TASKS_ROUTE) },
                onNewGroupClicked = {
                    navController.navigate(HomeAppDestinations.GROUP_ADD)
                }, onEditGroupClicked = {
                    vM.toEditGroup = it;
                    navController.navigate(HomeAppDestinations.GROUP_EDIT)
                },
                onInvitesClicked = {
                    navController.navigate(HomeAppDestinations.ALLINV_ROUTE)
                }
            )
        }

        composable(HomeAppDestinations.GROUP_ADD) {
            NewGroup(vM = hiltViewModel(),
            onGroupCancel = {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            })
        }
        composable(HomeAppDestinations.INVITE_ROUTE) {
            HomeInvitePersonRoute(hiltViewModel(), {
                navController.navigate(BottomNavItem.Tasks.screen_route)
            },
                vM.selectedGroup
            )
        }

        composable(HomeAppDestinations.ALLINV_ROUTE) {
            HomeInvitesRoute(vM = hiltViewModel(),
                {vM.selectedInvite = it;
                    navController.navigate(HomeAppDestinations.INVITE_DETAIL)},
                {navController.navigate(HomeAppDestinations.GROUP_ROUTE)}
            )
        }

        composable(HomeAppDestinations.INVITE_DETAIL) {
            DetailInviteRoute(
                vM = hiltViewModel(),
                invite = vM.selectedInvite,
                {navController.navigate(HomeAppDestinations.GROUP_ROUTE)}
            )
        }

        composable(HomeAppDestinations.GROUP_EDIT) {
            EditGroup(vM = hiltViewModel(),
                onGroupCancel = {
                    navController.navigate(HomeAppDestinations.GROUP_ROUTE)
                }, g = vM.toEditGroup)
        }

        composable(BottomNavItem.Overview.screen_route) {
            Calendar()
        }

        composable(route = BottomNavItem.Tasks.screen_route) {

            HomeTasksRoute(vM = hiltViewModel(), onBackPressed = {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            },
                onAddTask = {navController.navigate(HomeAppDestinations.TASK_ADD)},
                onInvitePerson = {navController.navigate(HomeAppDestinations.INVITE_ROUTE)},
                group = vM.selectedGroup)
        }

        composable(HomeAppDestinations.TASK_ADD) {
            AddTaskRoute(vM = hiltViewModel(), vM.selectedGroup, onCancelled = {
                navController.navigate(BottomNavItem.Tasks.screen_route)
            })
        }

        composable(BottomNavItem.Settings.screen_route){
            HomeSettingsRoute( onProfileClicked = {
                navController.navigate(HomeAppDestinations.PROFILE_ROUTE)
            })
        }

        composable(HomeAppDestinations.PROFILE_ROUTE) {
            HomeProfileRoute(
                context,
                {navController.navigate(BottomNavItem.Settings.screen_route)},
                {navController.navigate(HomeAppDestinations.LOGIN_ROUTE)}
            )
        }
    }
}