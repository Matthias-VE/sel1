package com.heppihome.viewmodels.users

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.heppihome.MainActivity
import com.heppihome.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeProfileViewModel @Inject constructor(private val rep : HomeRepository)
    : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun logout(c : Context, onLogout : () -> Unit) {
        _isLoading.value = true
        AuthUI.getInstance().signOut(c).addOnCompleteListener {
            _isLoading.value = false
            onLogout()
        }
    }
}