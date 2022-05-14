package com.heppihome.ui.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.internal.ComposableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.heppihome.data.models.Constants
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
                }, onSettingsPressed = {
                    navController.navigate(BottomNavItem.Settings.screen_route)
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
            ContentWithNavbar(navController) {
                HomeOverViewRoute(vM = hiltViewModel(), vM.selectedGroup)
            }
        }

        composable(HomeAppDestinations.SHOP_ROUTE) {
            ContentWithNavbar(navController) {
                HomeShopRoute(hiltViewModel(),vM.selectedGroup)
            }
        }

        composable(HomeAppDestinations.TASKS_ROUTE) {
            ContentWithNavbar(navController) {
                HomeTasksRoute(vM = hiltViewModel(), onBackPressed = {
                    navController.navigate(HomeAppDestinations.GROUP_ROUTE)
                },
                    onAddTask = {navController.navigate(HomeAppDestinations.TASK_ADD)},
                    onInvitePerson = {navController.navigate(HomeAppDestinations.INVITE_ROUTE)},
                    group = vM.selectedGroup)
            }
        }

        composable(HomeAppDestinations.TASK_ADD) {
            AddTaskRoute(vM = hiltViewModel(), vM.selectedGroup, onCancelled = {
                navController.navigate(BottomNavItem.Tasks.screen_route)
            })
        }

        composable(HomeAppDestinations.SETTINGS_ROUTE){
            HomeSettingsRoute(
                onBackPressed = {
                                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
                },
                onProfileClicked = {
                    navController.navigate(HomeAppDestinations.PROFILE_ROUTE)
                }
            )
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

@Composable
fun ContentWithNavbar(navController : NavController, content : @Composable() (BoxScope.() -> Unit)) {
    Scaffold(
        bottomBar = {BottomNavigation(navController) }
    ) {
        Box(modifier = Modifier.padding(it), content = content)
    }
}