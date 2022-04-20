import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = BottomNavItem.Tasks.screen_route) {

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