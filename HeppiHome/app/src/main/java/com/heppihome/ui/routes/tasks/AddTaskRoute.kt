package com.heppihome.ui.routes.tasks

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.ui.components.Header
import com.heppihome.ui.components.InputField
import com.heppihome.viewmodels.tasks.AddTaskViewModel

@Composable
fun AddTaskRoute(
    vM : AddTaskViewModel = hiltViewModel(),
    group : Group,
    onCancelled: () -> Unit
) {
    vM.usersInGroup(group)
    val name by vM.name.collectAsState()
    val users by vM.users.collectAsState()
    val usersInGroup by vM.usersInGroup.collectAsState()
    val deadline by vM.deadline.collectAsState()

    AddTaskScreen(
        name,
        deadline,
        users,
        usersInGroup,
        vM::checkUser,
        onCancelled,
        {vM.updateName(it)},
        {vM.addTask(group); onCancelled()}
    )
}
@Composable
fun AddTaskScreen(
    name : String,
    deadline : Timestamp,
    users: List<String>,
    usersInGroup : List<User>,
    onCheckUser: (User, Boolean) -> Unit,
    onCancelled : () -> Unit,
    onNameChanged : (String) -> Unit,
    onSaveTask : () -> Unit
) {
    Column() {
        Header("Add Task", onCancelled)
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
                InputField(name = "Task", description = name, onNameChanged)
                UserSelection(users = usersInGroup, users, onCheckUser = onCheckUser)
                Log.i("AddTaskRoute", "users size: " + users.size)
                Button(onClick = onSaveTask , enabled = users.isNotEmpty(),
                    modifier = Modifier.padding(10.dp)) {
                    Text("Add")
                }
        }
    }

}

@Composable
fun UserSelection(users : List<User>, selected : List<String>, onCheckUser : (User, Boolean) -> Unit) {
    Text("This task is for:", fontSize = MaterialTheme.typography.body1.fontSize, modifier = Modifier.padding(10.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
        for (user in users) {
            var checked by remember { mutableStateOf(user.id in selected)}
            Row {
                Checkbox(checked = checked, onCheckedChange = {checked = it; onCheckUser(user, it)})
                Text(
                    text = user.name,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                )
            }
        }
    }
}

