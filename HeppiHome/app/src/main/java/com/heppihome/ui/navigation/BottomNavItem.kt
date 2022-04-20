
sealed class BottomNavItem(var title: String, var icon:Int, var screen_route:String){

    object Tasks: BottomNavItem("Tasks", android.R.drawable.ic_menu_edit, "tasks")
    object Overview: BottomNavItem("Overview", android.R.drawable.ic_menu_my_calendar, "calendar")
    object Settings: BottomNavItem("Settings", android.R.drawable.ic_menu_preferences, "settings")
}