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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import com.heppihome.data.models.Task
import com.heppihome.data.models.User

import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.TasksViewModel
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
                   val vm : TasksViewModel = viewModel()

                   val tasks by vm.tasks.collectAsState()


                   MainTest(tasks, !vm.loadingPosted.value, { vm.addTask(it) }) {
                       vm.toggleTask(
                           it
                       )
                   }
                }
            }
        }
    }
}


@Composable
fun MainTest(tasks: List<Task>, addEnabled: Boolean, onAdded: (Task) -> Unit, onChecked: (Task) -> Unit) {
    Column() {
        TopAppBar() {
            Text(text = "Heppi Home", modifier = Modifier.padding(horizontal = 16.dp))
        }
        Greeting("Meneer den Alien")
        TestFirestore(tasks,addEnabled, onAdded, onChecked)
    }
}

@Composable
fun TestFirestore(tasks : List<Task>, addEnabled: Boolean, onAdded : (Task) -> Unit, onChecked: (Task) -> Unit) {
    Column() {
        for (t in tasks) {
            Row() {
                Checkbox(checked = t.done, onCheckedChange = {onChecked(t)})
                Text(text= t.text)
            }
        }
        Button(onClick = {onAdded(Task("yeetus", false, Timestamp.now(),listOf(User("test", "test@gmail.com"))))}, enabled = addEnabled) {
            Text(text = "Add Task")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HeppiHomeTheme {
        Greeting("Android")
    }
}