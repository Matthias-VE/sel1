package com.heppihome.ui.routes

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.data.models.User
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.users.HomeProfileViewModel

@Composable
fun HomeProfileRoute(
    context : Context,
    onBackPressed : () -> Unit,
    onLogout : () -> Unit
) {
    val vM : HomeProfileViewModel = hiltViewModel()

    HomeProfileScreen(user = vM.getUser(), onBackPressed) { vM.logout(context, onLogout)}
}

@Composable
fun HomeProfileScreen(user : User, onBackPressed: () -> Unit, onLogoutPressed : () -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            Topbar(title = stringResource(R.string.Profile), onBackPressed = onBackPressed)
            ProfileInformation(user.email, user.name)
            Logout(onLogoutPressed = onLogoutPressed)
        }

}

@Composable
fun DisplayText(s : String) {
    Text(s,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
fun ProfileInformation(email: String, name: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        DisplayText(s = name)
        DisplayText(s = email)
    }
}

@Composable
fun Logout(onLogoutPressed: () -> Unit) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
        ) {
        Button(
            onClick = onLogoutPressed,
        modifier = Modifier.scale(1.3F)
        ) {
            Text(stringResource(R.string.LogOut))
        }
    }
}

@Composable
fun Loading() {
    Text(
            stringResource(R.string.Loading),
    fontSize = 40.sp,
    fontWeight = FontWeight.Bold)
}

@Preview(showBackground = true)
@Composable
fun HomeProfileRoutePreview() {
    HomeProfileScreen(User(), {}, {})
}