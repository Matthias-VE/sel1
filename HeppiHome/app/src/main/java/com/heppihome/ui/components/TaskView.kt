package com.heppihome.ui.components

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
          format : SimpleDateFormat
) {

    Column {
        TopbarWithOptions(group.name,expandMenu, toggleMenu,
            onBackPressed = onBackPressed,
            listOf(stringResource(R.string.Invite)),
            listOf(onInvitePerson)
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(1) {
                Day("today", tasksToday, onChecked, format)
                Day("tomorrow", tasksTomorrow, onChecked, format)
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun Day(day : String, tasks : List<Task>, onChecked : (Task) -> Unit, format : SimpleDateFormat){
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
                for (task in tasks) {
                    Row {
                        Checkbox(checked = task.done, onCheckedChange = {onChecked(task)})
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


