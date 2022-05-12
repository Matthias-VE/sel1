package com.heppihome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.heppihome.data.test.PopulateDB
import com.heppihome.ui.navigation.HomeNavGraph

import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import com.heppihome.viewmodels.tasks.HomeTasksViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val TAG = MainActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeppiHomeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Population of database with dummy data.

                    val viewmTask : HomeTasksViewModel = hiltViewModel()
                    val viewmGroup : HomeGroupViewModel = hiltViewModel()
                    val p = PopulateDB(viewmTask, viewmGroup)
                    // true does the populate, false does nothing. For quick enabling / disabling.
                    p.Populate(false)

                    val navController = rememberNavController()
                    val context = this

                    Scaffold(
                        bottomBar = { com.heppihome.ui.navigation.BottomNavigation(navController = navController) }) {
                        Box(modifier = Modifier.padding(it)) {
                            HomeNavGraph(
                                navController = navController, vM = hiltViewModel(), context = context
                            )
                        }
                    }
                }
            }
        }
    }
}

