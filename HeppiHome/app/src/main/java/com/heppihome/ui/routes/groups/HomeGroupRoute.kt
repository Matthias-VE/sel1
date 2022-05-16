package com.heppihome.ui.routes.groups

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.ui.components.ConfirmDialog
import com.heppihome.ui.components.TopbarWithOptionsNoBackArrow
import com.heppihome.ui.components.TopbarWithSettings
import com.heppihome.ui.navigation.HomeAppDestinations
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import kotlin.math.roundToInt


@Composable
fun HomeGroupRoute(
    vM : HomeGroupViewModel,
    onGroupClicked : () -> Unit,
    onNewGroupClicked : () -> Unit,
    onEditGroupClicked : (Group) -> Unit,
    onInvitesClicked: () -> Unit,
    onSettingsPressed: () -> Unit,
    onAllTasks : () -> Unit
) {
    vM.refreshGroups()
    val groups by vM.groups.collectAsState()
    val expanded by vM.expanded.collectAsState()

    HomeGroupScreen(
        groups,
        {vM.setGroup(it); onGroupClicked()},
        expanded,
        vM::expandGroupMenu,
        onNewGroupClicked,
        vM,
        onEditGroupClicked,
        vM::leaveGroup,
        onInvitesClicked,
        onSettingsPressed,
        onAllTasks
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
    onEditGroupClicked : (Group) -> Unit,
    onLeaveGroupClicked: (Group, Context) -> Unit,
    onInvitesClicked : () -> Unit,
    onSettingsPressed : () -> Unit,
    onAllTasks : () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopbarWithSettings(
            title = stringResource(id = R.string.Groups),
            onSettingsPressed = onSettingsPressed
        )

        Alltasks(onAllTasks)
        //Groups(groups, onGroupClicked, vM, onEditGroupClicked, onLeaveGroupClicked)
        AddButton(groups, onGroupClicked, vM, onEditGroupClicked, onLeaveGroupClicked, onNewGroupClicked, onInvitesClicked)
    }
}

@Composable
fun Alltasks(onAllTasks : () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.padding(5.dp))
            Row(modifier = Modifier.padding(10.dp).clickable { onAllTasks() }, horizontalArrangement = Arrangement.Center) {
                Text(stringResource(R.string.AllTasks), fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun Groups(groups : List<Group>, onGroupClicked: (Group) -> Unit, vM : HomeGroupViewModel,
           onEditGroupClicked : (Group) -> Unit,
           onLeaveGroupClicked : (Group, Context) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(groups) { group ->
            SideView(group, onGroupClicked, vM, onEditGroupClicked, onLeaveGroupClicked)
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
fun DropDown(expanded: Boolean, toggle: () -> Unit,
             onNewGroupClicked: () -> Unit,
             onInvitesClicked: () -> Unit
) {

    DropdownMenu(expanded = expanded, onDismissRequest = toggle) {
        DropdownMenuItem(onClick = onNewGroupClicked) {
            Text(stringResource(R.string.NewGroup))
        }
        DropdownMenuItem(onClick = onInvitesClicked) {
            Text(stringResource(R.string.Invites))
        }
    }

}

@Composable
fun DropdownIcon(expanded: Boolean, toggle: () -> Unit,
                 onNewGroupClicked: () -> Unit,
                 onInvitesClicked: () -> Unit
) {

    IconButton(onClick = toggle) {
        Icon(
            Icons.Default.MoreVert, contentDescription = "Options", modifier = Modifier
                .size(40.dp)
        )
        DropDown(expanded = expanded, toggle = toggle, onNewGroupClicked, onInvitesClicked)
    }
}

@Composable
fun AddButton(
    groups: List<Group>,
    onGroupClicked: (Group) -> Unit,
    vM: HomeGroupViewModel,
    onEditGroupClicked: (Group) -> Unit,
    onLeaveGroupClicked: (Group, Context) -> Unit,
    onNewGroupClicked: () -> Unit,
    onInvitesClicked: () -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick =  onNewGroupClicked ) {
            Icon(Icons.Default.Add, "add task button")
        }
    }, floatingActionButtonPosition = FabPosition.End) {
        Groups(groups, onGroupClicked, vM, onEditGroupClicked, onLeaveGroupClicked)
        //vM.refreshInvites()
        if (vM.checkInvites()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = onInvitesClicked, modifier = Modifier
                    .padding(10.dp)
                    .size(60.dp), shape = RoundedCornerShape(50)) {
                    Icon(Icons.Default.Notifications, "invite", Modifier.size(40.dp))
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SideView(g : Group, onGroupClicked: (Group) -> Unit, vM : HomeGroupViewModel,
             onEditGroupClicked: (Group) -> Unit,
             onLeaveGroupClicked : (Group, Context) -> Unit
) {

    val context = LocalContext.current

    val squareSize = 150.dp
    val swipeAbleState = rememberSwipeableState(initialValue = 0f)
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0f, sizePx to 1f)

    if (showDeleteConfirm) {
        ConfirmDialog(content = stringResource(R.string.WantToLeaveGroup),
            onDismiss = { showDeleteConfirm = false},
            onConfirm = {
                showDeleteConfirm = false
                onLeaveGroupClicked(g, context)
            }
        )
    }

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
                        FractionalThreshold(0.3f)
                    },
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
                        showDeleteConfirm = true },
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
