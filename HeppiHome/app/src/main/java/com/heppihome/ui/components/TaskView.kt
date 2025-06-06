package com.heppihome.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import java.text.SimpleDateFormat




//Task View
@ExperimentalMaterialApi
@Composable
fun Tasks(tasksToday: List<Task>, tasksTomorrow: List<Task>,
          expandMenu : Boolean, toggleMenu : () -> Unit,
          onChecked: (Task) -> Unit, group : Group,
          onBackPressed : () -> Unit,
          onInvitePerson : () -> Unit,
          format : SimpleDateFormat,
          isAdmin : Boolean,
          resignAsAdmin : () -> Unit,
          makeSomeoneAdmin : () -> Unit,
          onTaskPressed : (Task) -> Unit
) {
    val lists = Pair(mutableListOf(stringResource(R.string.Invite)), mutableListOf(onInvitePerson))

    if (isAdmin) {
        lists.first.add(stringResource(R.string.ResignAdmin))
        lists.first.add(stringResource(R.string.MakeAdmin))
        lists.second.add(resignAsAdmin)
        lists.second.add(makeSomeoneAdmin)
    }

    Column {
        TopbarWithOptions(group.name,expandMenu, toggleMenu,
            onBackPressed = onBackPressed,
            lists.first,
            lists.second
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(1) {
                Day(stringResource(R.string.Today), tasksToday, onChecked, format, onTaskPressed)
                Day(stringResource(R.string.Tomorrow), tasksTomorrow, onChecked, format, onTaskPressed)
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun Day(day : String, tasks : List<Task>, onChecked : (Task) -> Unit, format : SimpleDateFormat,
        onTaskPressed: (Task) -> Unit
){
    var expended by remember { mutableStateOf(true)}
    val rotation by animateFloatAsState(targetValue = if(expended) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = MaterialTheme.shapes.medium,

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ){
            Row(modifier = Modifier.clickable {
                expended = !expended
            }){
                Text(
                    modifier = Modifier.weight(6f),
                    text = day,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight =  FontWeight.Bold
                )
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(1f)
                        .rotate(rotation),
                    onClick = {
                        expended = !expended
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "DropDown Arrow"
                    )
                }
            }
            if(expended){
                if(tasks.isEmpty()){
                    Text(text = stringResource(R.string.NoTasks))
                }else {
                    for (task in tasks) {
                        Row(Modifier.clickable(true, onClick = {onTaskPressed(task)})) {
                            Checkbox(checked = task.done, onCheckedChange = { onChecked(task) })
                            Text(
                                text = task.text,
                                fontSize = MaterialTheme.typography.subtitle1.fontSize
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp), horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = format.format(task.deadline.toDate())
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}


