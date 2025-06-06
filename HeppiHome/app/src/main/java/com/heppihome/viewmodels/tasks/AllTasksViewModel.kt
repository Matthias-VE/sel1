package com.heppihome.viewmodels.tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.Util.DateUtil
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AllTasksViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    var calendar = GregorianCalendar()

    private val _date = MutableStateFlow(
        DateUtil.formatDate(
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )
    )

    val date = _date.asStateFlow()

    private val _groups : MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups : StateFlow<List<Group>> = _groups

    private var temp : MutableMap<Group, List<Task>> =mutableMapOf()

    private val _groupsWithTasks : MutableStateFlow<MutableMap<Group, List<Task>>> = MutableStateFlow(mutableMapOf())
    val groupsWithTasks : StateFlow<MutableMap<Group, List<Task>>> = _groupsWithTasks

    fun refreshGroups() {
        viewModelScope.launch {
            _groups.value = rep.getAllGroups()
        }
    }

    fun toggleTask(task: Task, group: Group) {
        viewModelScope.launch {
            rep.checkTask(task, group).collect {
            }
            getGroupsWithTasks()
        }
    }

    fun getGroupsWithTasks() {
        viewModelScope.launch {
            _groups.value = rep.getAllGroups()
        }
        viewModelScope.launch {
            for (group in groups.value) {
                getTasks(group)
            }
        }
    }

    private fun getTasks(group : Group) {
        viewModelScope.launch{
            rep.getTasksBetweenStartOfDayAnd24Hours(group, calendar).collect {
                when(it){
                    is ResultState.Success -> {
                        temp[group] = it.data
                        _groupsWithTasks.value = temp
                        Log.d("data", "gettasks succes: ${it.data} \n ${temp}")
                    }
                    else -> Unit
                }
            }
        }
    }

}