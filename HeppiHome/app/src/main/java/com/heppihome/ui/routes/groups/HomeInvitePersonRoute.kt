package com.heppihome.ui.routes.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.data.models.Group
import com.heppihome.ui.components.InputField
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.groups.InvitePersonViewModel

@Composable
fun HomeInvitePersonRoute(
    vM : InvitePersonViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    group : Group
){
    val email by vM.email.collectAsState()
    HomeInvitePersonScreen(email, onBackPressed, vM::onChangeEmail, {vM.invitePerson(group); onBackPressed()})
}
@Composable
fun HomeInvitePersonScreen(
    email : String,
    onBackPressed : () -> Unit,
    onEmailChanged : (String) -> Unit,
    onPersonInvited : () -> Unit
) {
    Column {
        Topbar(title = "Invite", expanded = false, toggle = {}, onBackPressed = onBackPressed, {})
        InputField(name = "email", description = email, onEmailChanged)
        Button(onClick = onPersonInvited, enabled = email.length > 3, modifier =
        Modifier.padding(10.dp)) {
            Text("Invite this person")
        }
    }
}