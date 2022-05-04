package com.heppihome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.heppihome.data.test.PopulateDB
import com.heppihome.ui.navigation.HomeNavGraph

import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import com.heppihome.viewmodels.HomeTasksViewModel
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
                    p.Populate()

                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { com.heppihome.ui.navigation.BottomNavigation(navController = navController) }) {
                        HomeNavGraph(navController = navController
                            , vM = hiltViewModel(), context = this
                        )
                    }
                }
            }
        }
    }
}

