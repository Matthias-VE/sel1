package com.heppihome.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.heppihome.ui.components.NewGroup

import com.heppihome.ui.components.EditGroup
import com.heppihome.ui.routes.*
import com.heppihome.ui.routes.admin.MakeAdminRoute
import com.heppihome.ui.routes.groups.DetailInviteRoute
import com.heppihome.ui.routes.groups.HomeGroupRoute
import com.heppihome.ui.routes.groups.HomeInvitePersonRoute
import com.heppihome.ui.routes.groups.HomeInvitesRoute
import com.heppihome.ui.routes.shop.AddShopItemRoute
import com.heppihome.ui.routes.shop.EditShopItemRoute
import com.heppihome.ui.routes.shop.HomeInventoryRoute
import com.heppihome.ui.routes.shop.HomeShopRoute
import com.heppihome.ui.routes.tasks.*
import com.heppihome.viewmodels.HomeMainViewModel
import java.util.*

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
            HomeGroupRoute(vM = hiltViewModel(), onGroupClicked = {
                navController.navigate(HomeAppDestinations.TASKS_ROUTE) },
                onNewGroupClicked = {
                    navController.navigate(HomeAppDestinations.GROUP_ADD)
                }, onEditGroupClicked = {
                    vM.toEditGroup = it
                    navController.navigate(HomeAppDestinations.GROUP_EDIT)
                },
                onInvitesClicked = {
                    navController.navigate(HomeAppDestinations.ALLINV_ROUTE)
                }, onSettingsPressed = {
                    navController.navigate(HomeAppDestinations.PROFILE_ROUTE)
                },
                onAllTasks = {navController.navigate(HomeAppDestinations.ALL_TASKS)}
            )
        }

        composable(HomeAppDestinations.ALL_TASKS){
            AllTasksRoute(vM = hiltViewModel()) {navController.navigate(HomeAppDestinations.GROUP_ROUTE) }
        }

        composable(HomeAppDestinations.TASK_EDIT_ROUTE) {
            EditTaskRoute(onCancelled = {
                vM.selectedTask = it
                navController.navigate(HomeAppDestinations.TASK_DETAIL_ROUTE)
            },
                task = vM.selectedTask
            )
        }

        composable(HomeAppDestinations.TASK_DETAIL_ROUTE) {
            TaskDetailRoute(
                onGoBack = {
                           if (vM.fromTasks) {
                               navController.navigate(HomeAppDestinations.TASKS_ROUTE)
                           } else {
                               navController.navigate(HomeAppDestinations.DATE_TASKS)
                           }
                },
                task = vM.selectedTask,
                onEditPressed = { navController.navigate(HomeAppDestinations.TASK_EDIT_ROUTE) })
        }

        composable(BottomNavItem.Overview.screen_route) {
            ContentWithNavbar(navController) {
                HomeOverViewRoute(vM = hiltViewModel()
                ) {
                    vM.calendar = it
                    navController.navigate(HomeAppDestinations.DATE_TASKS)
                }
            }
        }

        composable(HomeAppDestinations.DATE_TASKS){
            DateTasksRoute(vM = hiltViewModel(),
                onBackPressed = {navController.navigate(BottomNavItem.Overview.screen_route)},
                vM.calendar,
                onTaskPressed = {
                    vM.selectedTask = it
                    vM.fromTasks = false
                    navController.navigate(HomeAppDestinations.TASK_DETAIL_ROUTE)}
            )
        }

        composable(HomeAppDestinations.GROUP_ADD) {
            NewGroup(vM = hiltViewModel(),
            onGroupCancel = {
                navController.navigate(HomeAppDestinations.GROUP_ROUTE)
            })
        }
        composable(HomeAppDestinations.INVITE_ROUTE) {
            HomeInvitePersonRoute(hiltViewModel()
            ) {
                navController.navigate(BottomNavItem.Tasks.screen_route)
            }
        }

        composable(HomeAppDestinations.ALLINV_ROUTE) {
            HomeInvitesRoute(vM = hiltViewModel(),
                {vM.selectedInvite = it
                    navController.navigate(HomeAppDestinations.INVITE_DETAIL)},
                {navController.navigate(HomeAppDestinations.GROUP_ROUTE)}
            )
        }

        composable(HomeAppDestinations.INVITE_DETAIL) {
            DetailInviteRoute(
                vM = hiltViewModel(),
                invite = vM.selectedInvite
            ) { navController.navigate(HomeAppDestinations.GROUP_ROUTE) }
        }

        composable(HomeAppDestinations.GROUP_EDIT) {
            EditGroup(vM = hiltViewModel(),
                onGroupCancel = {
                    navController.navigate(HomeAppDestinations.GROUP_ROUTE)
                }, g = vM.toEditGroup)
        }

        composable(HomeAppDestinations.SHOP_ROUTE) {
            ContentWithNavbar(navController) {
                HomeShopRoute(hiltViewModel(),
                    {
                        vM.toEditShopItem = it
                        navController.navigate(HomeAppDestinations.EDIT_SHOPITEM_ROUTE)},
                    {navController.navigate(HomeAppDestinations.INVENTORY_ROUTE)},
                    {navController.navigate(HomeAppDestinations.ADD_SHOPITEM_ROUTE)}
                )
            }
        }

        composable(HomeAppDestinations.ADD_SHOPITEM_ROUTE) {
            AddShopItemRoute {
                navController.navigate(HomeAppDestinations.SHOP_ROUTE)
            }
        }

        composable(HomeAppDestinations.EDIT_SHOPITEM_ROUTE) {
            EditShopItemRoute(goBackToShop = {
               navController.navigate(HomeAppDestinations.SHOP_ROUTE)
            }, shopItem = vM.toEditShopItem)
        }

        composable(HomeAppDestinations.INVENTORY_ROUTE) {
            ContentWithNavbar(navController = navController) {
                HomeInventoryRoute(
                    vM = hiltViewModel(),
                    onBackPressed = { navController.navigate(HomeAppDestinations.SHOP_ROUTE) }
                )
            }
        }

        composable(HomeAppDestinations.MAKE_ADMIN_ROUTE) {
            MakeAdminRoute(
                onBackPressed = {navController.navigate(HomeAppDestinations.TASKS_ROUTE)}
            )
        }

        composable(HomeAppDestinations.TASKS_ROUTE) {
            ContentWithNavbar(navController) {
                HomeTasksRoute(vM = hiltViewModel(), onBackPressed = {
                    navController.navigate(HomeAppDestinations.GROUP_ROUTE)
                },
                    onAddTask = {navController.navigate(HomeAppDestinations.TASK_ADD)},
                    onInvitePerson = {navController.navigate(HomeAppDestinations.INVITE_ROUTE)},
                    onMakeSomeoneAdmin = {navController.navigate(HomeAppDestinations.MAKE_ADMIN_ROUTE)},
                    onTaskPressed = {
                        vM.selectedTask = it
                        vM.fromTasks = true
                        navController.navigate(HomeAppDestinations.TASK_DETAIL_ROUTE)}
                )
            }
        }

        composable(HomeAppDestinations.TASK_ADD) {
            AddTaskRoute(vM = hiltViewModel(), onCancelled = {
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
                {navController.navigate(HomeAppDestinations.GROUP_ROUTE)},
                {navController.navigate(HomeAppDestinations.LOGIN_ROUTE)}
            )
        }
    }
}

@Composable
fun ContentWithNavbar(navController : NavController, content : @Composable (BoxScope.() -> Unit)) {
    Scaffold(
        bottomBar = {BottomNavigation(navController) }
    ) {
        Box(modifier = Modifier.padding(it), content = content)
    }

}