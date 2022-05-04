package com.heppihome.ui.routes

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.FrameMetricsAggregator.ANIMATION_DURATION
import com.google.android.gms.common.config.GservicesValue.value
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.ui.theme.Purple200
import com.heppihome.ui.theme.Purple500
import com.heppihome.viewmodels.HomeGroupViewModel
import kotlin.math.roundToInt


@Composable
fun HomeGroupRoute(
    vM : HomeGroupViewModel,
    onGroupClicked : (Group) -> Unit,
    onNewGroupClicked : () -> Unit,
    onEditGroupClicked : (Group) -> Unit
) {
    vM.refreshGroups()
    val groups by vM.groups.collectAsState()
    val expanded by vM.expanded.collectAsState()

    HomeGroupScreen(
        groups,
        onGroupClicked,
        expanded,
        { vM.expandGroupMenu() },
        onNewGroupClicked,
        vM,
        onEditGroupClicked
    )
}

@Composable
fun HomeGroupScreen(
    groups: List<Group>,
    onGroupClicked: (Group) -> Unit,
    expanded: Boolean,
    toggle: () -> Unit,
    onNewGroupClicked : () -> Unit,
    vM : HomeGroupViewModel,
    onEditGroupClicked : (Group) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Header(expanded, toggle, onNewGroupClicked)
        Alltasks()
        Groups(groups, onGroupClicked, vM, onEditGroupClicked)
    }
}

@Composable
fun Header(expanded: Boolean, toggle: () -> Unit, onNewGroupClicked: () -> Unit) {
    
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp)) {
                Text("Groups", fontSize = 30.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End) {
                DropdownIcon(expanded = expanded, toggle = toggle, onNewGroupClicked)
            }
        }
    }
}

@Composable
fun Alltasks() {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
            Text("All tasks", fontSize = 30.sp)
        }
    }
}

@Composable
fun Groups(groups : List<Group>, onGroupClicked: (Group) -> Unit, vM : HomeGroupViewModel, onEditGroupClicked : (Group) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(groups) { group ->
            SideView(group, onGroupClicked, vM, onEditGroupClicked)
        }
    }

}

@Composable
fun Groupdetail(g : Group, onGroupClicked: (Group) -> Unit) {
    Button(onClick = {onGroupClicked(g)}) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, Color.Gray))
                .padding(20.dp)
        ) {
            Text(text = g.name, fontSize = 30.sp)
            Text(text = g.description, fontSize = 20.sp, color = Color.Gray)
        }
    }
}

@Composable
fun DropDown(expanded: Boolean, toggle: () -> Unit, onNewGroupClicked: () -> Unit) {

    DropdownMenu(expanded = expanded, onDismissRequest = toggle) {
        DropdownMenuItem(onClick = onNewGroupClicked) {
            Text("New Group")
        }
        DropdownMenuItem(onClick = { println("Join Group") }) {
            Text("Join Group")
        }
        DropdownMenuItem(onClick = { println("Leave Group") }) {
            Text("Leave Group")
        }
    }

}

@Composable
fun DropdownIcon(expanded: Boolean, toggle: () -> Unit, onNewGroupClicked: () -> Unit) {

    IconButton(onClick = toggle) {
        Icon(
            Icons.Default.MoreVert, contentDescription = "Options", modifier = Modifier
                .size(40.dp)
        )
        DropDown(expanded = expanded, toggle = toggle, onNewGroupClicked)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SideView(g : Group, onGroupClicked: (Group) -> Unit, vM : HomeGroupViewModel, onEditGroupClicked: (Group) -> Unit) {

    val squareSize = 150.dp
    val swipeAbleState = rememberSwipeableState(initialValue = 0f)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0f, sizePx to 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.LightGray)
                .swipeable(
                    state = swipeAbleState,
                    anchors = anchors,
                    thresholds = { _, _ ->
                        FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(
                    onClick = {onEditGroupClicked(g)},
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Green
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                IconButton(
                    onClick = {
                        println("remove") },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            swipeAbleState.offset.value.roundToInt(), 0
                        )
                    }
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .height(150.dp)
                    .fillMaxHeight()
                    .clickable { onGroupClicked(g) }
                    .background(MaterialTheme.colors.secondary)
                    .align(Alignment.CenterStart)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Column {
                            Text(
                                text = g.name,
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.padding(10.dp))

                            Text(
                                text = g.description,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
