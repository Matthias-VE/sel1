package com.heppihome.viewmodels.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Invite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitesViewModel @Inject constructor(private val rep : HomeRepository)
    : ViewModel(){

    private val _invites = MutableStateFlow<List<Invite>>(emptyList())
    val invites = _invites.asStateFlow()

    private val _currentInvite = MutableStateFlow(Invite())
    val currentInvite = _currentInvite.asStateFlow()

    init {
        refreshInvites()
    }

    private fun refreshInvites() {
        viewModelScope.launch {
            _invites.value = rep.getAllInvites()
        }
    }

    fun acceptInvite(invite: Invite) {
        viewModelScope.launch {
            rep.acceptInvite(invite)
            refreshInvites()
        }
    }

    fun removeInviteFromList(inv : Invite) {
        viewModelScope.launch {
            rep.declineInvite(inv)
            refreshInvites()
        }
    }

}