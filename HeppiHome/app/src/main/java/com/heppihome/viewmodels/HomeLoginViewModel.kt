package com.heppihome.viewmodels

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.heppihome.MainActivity
import com.heppihome.R
import com.heppihome.data.HomeRepository
import com.heppihome.ui.authentication.AuthResultCode
import com.heppihome.ui.authentication.FirebaseAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeLoginViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel(), FirebaseAuthManager {
    private val TAG = this::class.java.name

    private val _isLoggedIn = MutableStateFlow(!rep.isAnonymousUser())
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _authResultCode = MutableStateFlow(AuthResultCode.NOT_APPLICABLE)
    val authResultCode = _authResultCode.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    override fun buildLoginIntent(): Intent {
        return AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.EmailBuilder().build()
                )
            )
            .enableAnonymousUsersAutoUpgrade()
            .setLogo(R.mipmap.ic_launcher)
            .build()
    }

    override fun onLoginResult(result: FirebaseAuthUIAuthenticationResult) {
        Log.d(TAG, "onLoginResult triggered")
        Log.d(TAG, result.toString())

        val response : IdpResponse? = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK) {
            _user.value = rep.getUser()

            _isLoggedIn.value = true
            _authResultCode.value = AuthResultCode.OK

            Log.d(TAG, "login succesful")
            return
        }

        val userCancelled = (response == null)
        if (userCancelled) {
            _authResultCode.value = AuthResultCode.CANCELLED
            Log.d(TAG, "login cancelled")
            return
        }

        when (response?.error?.errorCode) {
            ErrorCodes.NO_NETWORK -> {
                _authResultCode.value = AuthResultCode.NO_NETWORK

            }
            ErrorCodes.ANONYMOUS_UPGRADE_MERGE_CONFLICT -> {

            }
            else -> {
                Log.d(TAG, "login failed")
                _authResultCode.value = AuthResultCode.ERROR
            }
        }
    }
}