package com.heppihome.viewmodels

import android.widget.CalendarView
import androidx.compose.runtime.collectAsState
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
    private val cal = GregorianCalendar()

    private val _groups : MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups : StateFlow<List<Group>> = _groups

    private var groupsWithTasks : MutableMap<Group, List<Task>> = mutableMapOf()


    fun getGroupsWithTasks() = MutableStateFlow(groupsWithTasks)

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

    fun getAllTasks(groups : List<Group>, calendar : Calendar){
        for (group in groups){
            getTasks(group, calendar)
        }
    }


    fun getTasks(group : Group, calendar : Calendar) {
        viewModelScope.launch{
            rep.getTasksBetweenStartOfDayAnd24Hours(group, calendar).collect {
                when(it){
                    is ResultState.Success -> groupsWithTasks[group] = it.data
                    else -> Unit
                }
            }
        }
    }
}