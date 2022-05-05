package com.heppihome.viewmodels.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitePersonViewModel @Inject constructor(private val rep : HomeRepository)
    : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun onChangeEmail(s : String) {
        _email.value = s
    }

    // invite this email for this group
    fun invitePerson(group : Group) {
        var succes = false
        viewModelScope.launch {
            succes = rep.sendInviteTo(_email.value, group)
        }

    }
}