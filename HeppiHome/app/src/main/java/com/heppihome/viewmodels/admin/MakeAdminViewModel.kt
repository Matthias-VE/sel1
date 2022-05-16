package com.heppihome.viewmodels.admin


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakeAdminViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _feedback = MutableStateFlow(false)
    private val _users = MutableStateFlow(emptyList<User>())

    val feedback = _feedback.asStateFlow()
    val users = _users.asStateFlow()
    val admins = rep.admins

    fun makeAdmin(user : User) {
        viewModelScope.launch {
            rep.addAdminToGroup(user).collect {
                if (it is ResultState.Success) _feedback.value = true
            }
        }
    }

    fun usersInGroup() {
        viewModelScope.launch {
            _users.value = rep.getAllUsersInGroup(rep.selectedGroup.users)
        }
    }

    fun resetFeedback() {
        _feedback.value = false
    }
}