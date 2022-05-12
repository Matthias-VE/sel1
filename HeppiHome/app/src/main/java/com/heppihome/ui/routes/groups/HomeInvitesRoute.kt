package com.heppihome.ui.routes.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Invite
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import com.heppihome.viewmodels.groups.InvitesViewModel
import kotlin.math.roundToInt

@Composable
fun HomeInvitesRoute(
    vM : InvitesViewModel = hiltViewModel(),
    onInviteClicked: (Invite) -> Unit,
    onBackPressed : () -> Unit
) {
    val invites by vM.invites.collectAsState()
    HomeInvitesScreen(
        invites = invites,
        onInviteClicked = {onInviteClicked(it)},
        onDeclineInviteClicked = vM::removeInviteFromList,
        onAcceptInviteClicked = vM::acceptInvite,
        onBackPressed = onBackPressed
    )
}

@Composable
fun HomeInvitesScreen(invites : List<Invite>, onInviteClicked: (Invite) -> Unit,
                      onDeclineInviteClicked: (Invite) -> Unit,
                      onAcceptInviteClicked: (Invite) -> Unit,
                      onBackPressed: () -> Unit
                      ) {
    Column() {
        Topbar(title = stringResource(R.string.YourInvites), onBackPressed = onBackPressed)
        if (invites.isEmpty()) {
            Text(stringResource(R.string.NoInvites))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(invites) { i ->
                    SideView(i, onInviteClicked, onDeclineInviteClicked, onAcceptInviteClicked)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SideView(i : Invite, onInviteClicked: (Invite) -> Unit, onDeclineInviteClicked: (Invite) -> Unit,
             onAcceptInviteClicked: (Invite) -> Unit) {

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
                        FractionalThreshold(0.3f)
                    },
                    orientation = Orientation.Horizontal
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(
                    onClick = {onAcceptInviteClicked(i)},
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Accept",
                        tint = Color.Green
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                IconButton(
                    onClick = {
                        onDeclineInviteClicked(i) },
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
                    .clickable { onInviteClicked(i) }
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
                                text = stringResource(R.string.InviteFrom) + i.fromEmail,
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }
                }
            }
        }
    }
}