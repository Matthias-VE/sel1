import compose.R

sealed class BottomNavItem(var title: String, var icon:Int, var screen_route:String){

    object Tasks: BottomNavItem("Tasks", R.drawable.ic_job, "tasks")
    object Overview: BottomNavItem("Overview", R.drawable.ic_menu_my_calendar, "calendar")
    object Settings: BottomNavItem("Settings", R.drawable.ic_menu_preferences, "settings")
}