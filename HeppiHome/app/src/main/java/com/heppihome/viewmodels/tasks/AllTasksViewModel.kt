package com.heppihome.viewmodels.tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AllTasksViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _groups : MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups : StateFlow<List<Group>> = _groups

    private var _groupsWithTasks : MutableMap<Group, List<Task>> = mutableMapOf()
    var groupsWithTasks : StateFlow<MutableMap<Group, List<Task>>> = MutableStateFlow(_groupsWithTasks)

    fun refreshGroups() {
        viewModelScope.launch {
            _groups.value = rep.getAllGroups()
        }
    }

    fun updateGroupsWithTasks(groups : List<Group>, calendar : Calendar) {
        viewModelScope.launch {
            for (group in groups) {
                Log.d("groep", "$group")
                getTasks(group, calendar)
            }
        }
    }


    private fun getTasks(group : Group, calendar : Calendar) {
        viewModelScope.launch{
            Log.i("taskdate", "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH)}-${calendar.get(
                Calendar.YEAR)}")
            rep.getTasksBetweenStartOfDayAnd24Hours(group, calendar).collect {
                when(it){
                    is ResultState.Success -> {
                        Log.d("data", "gettasks succes: ${it.data}")
                        _groupsWithTasks[group] = it.data}
                    else -> {
                        Log.d("fail", "gettasks failed")
                        Unit}
                }
            }
        }
    }
}