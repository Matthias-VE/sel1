import NavigationGraph


class BottomNavActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Budle?){
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
        }
    }
}

@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold( navBar = { BottomNavigation(navController = navController) }) {
        NavigationGraph(navController = navController)
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Overview,
        BottomNavItem.Tasks,
        BottomNavItem.Settings)

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.green_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 9.sp) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White,
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let {screen_route ->
                            popUpTo(screen_route) {saveState = true}
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview(){
    MainScreenView()
}
