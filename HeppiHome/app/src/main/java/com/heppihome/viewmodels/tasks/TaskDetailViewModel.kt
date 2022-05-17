package com.heppihome.viewmodels.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    val isAdmin = rep.isAdmin

    private val _users = MutableStateFlow(emptyList<User>())
    val users = _users.asStateFlow()


    fun getUsers(t : Task) {
        viewModelScope.launch {
            _users.value =  rep.getAllUsersInGroup(t.users)
        }
    }

}