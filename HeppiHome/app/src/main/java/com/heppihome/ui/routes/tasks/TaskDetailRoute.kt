package com.heppihome.ui.routes.tasks

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.Util.DateUtil
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.ui.components.InputField
import com.heppihome.ui.components.InputNumberField
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.tasks.TaskDetailViewModel
import java.text.SimpleDateFormat

@Composable
fun TaskDetailRoute(
    
    onGoBack : () -> Unit,
    task : Task,
    onEditPressed : () -> Unit,
    vM : TaskDetailViewModel = hiltViewModel()
) {
    vM.getUsers(task)
    
    val isAdmin by vM.isAdmin.collectAsState()
    val usersInTask by vM.users.collectAsState()
    
    TaskDetailScreen(
        onGoBack,
        task,
        usersInTask,
        isAdmin,
        onEditPressed
    )
    
}

@Composable
fun TaskDetailScreen(
    onGoBack: () -> Unit,
    task : Task,
    users : List<User>,
    isAdmin : Boolean,
    onEditPressed: () -> Unit
) {
    Column() {
        Topbar(stringResource(id = R.string.Task), onGoBack)
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            
            TaskDetails(
                task, users,
                SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()),
                SimpleDateFormat("kk:mm", java.util.Locale.getDefault())
            )
            
            if (isAdmin) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onEditPressed, modifier = Modifier
                        .padding(10.dp)
                        .size(60.dp), shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.Edit, "edit", Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TaskDetails(task : Task, users : List<User>,
                formatDate : SimpleDateFormat,
                formatHours : SimpleDateFormat
) {
    LazyColumn(Modifier.padding(15.dp)) {
        items(1) {
            Text(task.text, style = MaterialTheme.typography.h6, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "${task.points}  ${stringResource(id = R.string.PointsShort)}",
                fontFamily = FontFamily(Font(R.font.partyconfetti_regular, FontWeight.Normal)),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.padding(15.dp))
            Text(stringResource(R.string.TaskForPerson), fontSize = MaterialTheme.typography.body1.fontSize)
            for (user in users) {
                Text(
                    text = user.name,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                )
            }
            Spacer(modifier = Modifier.padding(15.dp))
            val date = task.deadline.toDate()
            Text(text = "Deadline:")
            Text(text = formatDate.format(date))
            Text(text = formatHours.format(date))
        }
    }
}