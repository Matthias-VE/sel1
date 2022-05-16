package com.heppihome.ui.routes

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.heppihome.ui.authentication.AuthResultCode
import com.heppihome.viewmodels.users.HomeLoginViewModel

@Composable
fun HomeLoginRoute(
    vM : HomeLoginViewModel,
    onIsLoggedInAndNavigateOnce : () -> Unit

) {
    val isLoggedIn by vM.isLoggedIn.collectAsState()
    val authResultCode by vM.authResultCode.collectAsState()

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
        LaunchedEffect(Unit) {
            Log.i("Login Route", "The onIsLoggedIn is called")
            vM.setUser()
            onIsLoggedInAndNavigateOnce()
        }
    }
}
