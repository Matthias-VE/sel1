package com.heppihome.viewmodels.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.heppihome.Util.DateUtil
import com.heppihome.Util.NumberUtil
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val rep : HomeRepository)
    : ViewModel() {

    val calendar = GregorianCalendar()

    private val _name = MutableStateFlow<String>("")
    private val _points = MutableStateFlow("0")
    private val _users = MutableStateFlow<List<String>>(listOf(rep.user.id))
    private val _usersInGroup = MutableStateFlow<List<User>>(listOf(rep.user))
    private val _hours = MutableStateFlow<String>(
        DateUtil.formatHours(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    )
    private val _date = MutableStateFlow<String>(
        DateUtil.formatDate(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )
    )

    val isAdmin = rep.isAdmin

    val name = _name.asStateFlow()
    val points = _points.asStateFlow()
    val users = _users.asStateFlow()
    val usersInGroup = _usersInGroup.asStateFlow()
    val hours = _hours.asStateFlow()
    val date = _date.asStateFlow()


    fun usersInGroup() {
        viewModelScope.launch {
            _usersInGroup.value = rep.getAllUsersInGroup(rep.selectedGroup.users)
        }
    }

    fun updateName(s : String) {
        _name.value = s
    }

    fun updatePoints(p : String) {
        _points.value = p
    }

    fun checkUser(u : User, selected : Boolean) {
        if (selected) addUserToList(u)
        else removeUserFromList(u)
    }

    private fun addUserToList(u : User) {
        _users.value = _users.value + u.id
    }

    private fun removeUserFromList(u : User) {
        _users.value = _users.value.minus(u.id)
    }


    fun addTask() {
        val task = Task(_name.value, false, Timestamp(calendar.time),
            _users.value, NumberUtil.verifyInput(_points.value))
        viewModelScope.launch {
            rep.addTask(task).collect {  }
        }
    }

    fun updateHours(hour : Int, minutes : Int) {
        _hours.value = DateUtil.formatHours(hour, minutes)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minutes)
    }

    fun updateDate(day : Int, month : Int, year: Int) {
        _date.value = DateUtil.formatDate(day, month, year)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
    }
}