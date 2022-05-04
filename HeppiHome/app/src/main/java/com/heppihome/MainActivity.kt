package com.heppihome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.data.test.PopulateDB
import com.heppihome.ui.navigation.HomeNavGraph

import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.HomeGroupViewModel
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
                            , vM = hiltViewModel()
                        )
                    }
                }
            }
        }
    }
}

