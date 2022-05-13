package com.heppihome.viewmodels

import android.util.Log
import android.widget.CalendarView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.Util.DateUtil
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeOverviewViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
    val cal = GregorianCalendar()

    private val _groups : MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups : StateFlow<List<Group>> = _groups

    private var _groupsWithTasks : MutableMap<Group, List<Task>> = mutableMapOf()
    val groupsWithTasks : StateFlow<MutableMap<Group, List<Task>>> = MutableStateFlow(_groupsWithTasks)


    fun refreshGroups() {
        viewModelScope.launch {
            _groups.value = rep.getAllGroups()
        }
    }

    private val _date = MutableStateFlow(
        DateUtil.formatDate(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.YEAR)
        )
    )

    val date = _date.asStateFlow()

    fun onDateChange(
        calView : CalendarView,
        year : Int,
        month : Int,
        day : Int
    ) {
        _date.value = DateUtil.formatDate(day, month, year)
        cal.set(Calendar.DAY_OF_MONTH, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.YEAR, day)
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

    fun toggleTask(task: Task, group : Group) {
        viewModelScope.launch {
            rep.checkTask(task, group).collect {
            }
        }
    }
}