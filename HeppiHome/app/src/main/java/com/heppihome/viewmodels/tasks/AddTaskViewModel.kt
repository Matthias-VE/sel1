package com.heppihome.viewmodels.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val rep : HomeRepository)
    : ViewModel() {

    private val _name = MutableStateFlow<String>("")
    private val _users = MutableStateFlow<List<String>>(listOf(rep.user.id))
    private val _deadline = MutableStateFlow<Timestamp>(Timestamp.now())
    private val _usersInGroup = MutableStateFlow<List<User>>(listOf(rep.user))

    fun usersInGroup(g : Group) {
        viewModelScope.launch {
            _usersInGroup.value = rep.getAllUsersInGroup(g.users)
        }

    }

    val name = _name.asStateFlow()
    val users = _users.asStateFlow()
    val deadline = _deadline.asStateFlow()
    val usersInGroup = _usersInGroup.asStateFlow()

    fun updateName(s : String) {
        _name.value = s
    }

    fun checkUser(u : User, selected : Boolean) {
        if (selected) addUserToList(u)
        else removeUserFromList(u)
    }

    fun addUserToList(u : User) {
        _users.value = _users.value + u.id
    }

    fun removeUserFromList(u : User) {
        _users.value = _users.value.minus(u.id)
    }

    fun addTask(g : Group) {
        val task = Task(_name.value, false, _deadline.value, _users.value)
        viewModelScope.launch {
            rep.addTask(task, g).collect {  }
        }
    }

    //private val _deadline = MutableStateFlow<Timestamp>(Timestamp.now())
    private val _mHours = MutableStateFlow<String>("")
    private val _date = MutableStateFlow<String>("")
    //val isLoggedIn = _isLoggedIn.asStateFlow()
    val date = _date.asStateFlow()
    val hours = _mHours.asStateFlow()

    fun updateHours(s : String) {
        _mHours.value = s
    }

    fun updateDate(s : String) {
        _date.value = s
    }
}