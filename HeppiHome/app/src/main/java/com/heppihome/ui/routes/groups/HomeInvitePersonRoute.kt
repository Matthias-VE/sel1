package com.heppihome.ui.routes.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.ui.components.InputField
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.groups.InvitePersonViewModel

@Composable
fun HomeInvitePersonRoute(
    vM : InvitePersonViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
){
    val email by vM.email.collectAsState()
    HomeInvitePersonScreen(email, onBackPressed, vM::onChangeEmail, {vM.invitePerson(); onBackPressed()})
}
@Composable
fun HomeInvitePersonScreen(
    email : String,
    onBackPressed : () -> Unit,
    onEmailChanged : (String) -> Unit,
    onPersonInvited : () -> Unit
) {
    Column {
        Topbar(title = stringResource(R.string.Invite), onBackPressed = onBackPressed)
        InputField(name = stringResource(R.string.Email), description = email, onEmailChanged)
        Button(onClick = onPersonInvited, enabled = email.length > 3, modifier =
        Modifier.padding(10.dp)) {
            Text(stringResource(R.string.InvitePerson))
        }
    }
}