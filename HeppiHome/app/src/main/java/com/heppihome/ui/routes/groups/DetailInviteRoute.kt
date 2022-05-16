package com.heppihome.ui.routes.groups

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.data.models.Invite
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.groups.InvitesViewModel

@Composable
fun DetailInviteRoute(
    vM : InvitesViewModel = hiltViewModel(),
    invite : Invite,
    onBackPressed: () -> Unit
) {

    DetailInviteScreen(invite = invite, onAcceptInvite = {vM.acceptInvite(it); onBackPressed()},
        onDeclineInvite = {vM.removeInviteFromList(it); onBackPressed()}, onBackPressed = onBackPressed)
}

@Composable
fun DetailInviteScreen(
    invite : Invite,
    onAcceptInvite : (Invite) -> Unit,
    onDeclineInvite : (Invite) -> Unit,
    onBackPressed : () -> Unit
) {
    Column {
        Topbar(stringResource(R.string.Invite), onBackPressed)
        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.align(Alignment.TopCenter)) {
                Text(
                    stringResource(R.string.GotInvite) + invite.fromEmail,
                    style = MaterialTheme.typography.h6
                )
                Text(stringResource(R.string.AcceptOrDeclineInvite))
            }
            Row(Modifier.align(Alignment.Center)) {
                    Button(onClick = {onDeclineInvite(invite)}) {
                        Text(stringResource(R.string.Decline))
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Button(onClick = {onAcceptInvite(invite)}) {
                        Text(stringResource(R.string.Accept))
                    }
            }

        }
    }
}