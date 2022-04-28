package com.heppihome.ui.routes

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.firebase.auth.FirebaseUser
import com.heppihome.ui.authentication.AuthResultCode
import com.heppihome.viewmodels.HomeLoginViewModel

@Composable
fun HomeLoginRoute(
    vM : HomeLoginViewModel,
    onIsLoggedIn : (FirebaseUser) -> Unit
) {
    val isLoggedIn by vM.isLoggedIn.collectAsState()
    val authResultCode by vM.authResultCode.collectAsState()
    val user by vM.user.collectAsState()

    val loginLauncher = rememberLauncherForActivityResult(
        vM.buildLoginActivityResult()

    ) {
        if (it != null) {
            vM.onLoginResult(it)
        }
    }

    if (!isLoggedIn || authResultCode == AuthResultCode.CANCELLED) {
        LaunchedEffect(true) {
            loginLauncher.launch(vM.buildLoginIntent())
        }
    } else {
        onIsLoggedIn(user!!)
    }
}
