package com.heppihome.viewmodels.users

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
import com.heppihome.R
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.User
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

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _authResultCode = MutableStateFlow(AuthResultCode.NOT_APPLICABLE)
    val authResultCode = _authResultCode.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null
    )
    val user = _user.asStateFlow()


    init {
        viewModelScope.launch {
            _isLoggedIn.value = !rep.isAnonymousUser()
            _user.value = rep.getUser()
        }
    }

    fun fuToUser(fu : FirebaseUser) : User {
        var name = "default"
        var email = "default"
        fu.displayName?.let { name = it }
        fu.email?.let { email = it }
        return User(name, email, fu.uid)
    }

    fun setUser() {
        rep.user = fuToUser(user.value!!)
    }

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

            viewModelScope.launch {
                _user.value = rep.getUser()
                rep.saveUser(fuToUser(_user.value!!))
            }

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