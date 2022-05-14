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
    var cal = GregorianCalendar()

    private var selectedGroup = Group()

    private val _tasks : MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())

    val tasks : StateFlow<List<Task>> = _tasks

    fun setGroup(g : Group) {
        selectedGroup = g
    }

    private val _date = MutableStateFlow(
        DateUtil.formatDate(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.YEAR)
        )
    )

    fun resetDate(){
        _tasks.value = emptyList()
        cal = GregorianCalendar()
        _date.value = DateUtil.formatDate(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.YEAR)
        )
    }

    val date = _date.asStateFlow()

    fun onDateChange(
        calView : CalendarView,
        year : Int,
        month : Int,
        day : Int
    ) {
        Log.i("datum", "$day, $month, $year")
        _date.value = DateUtil.formatDate(day, month, year)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.YEAR, year)
        getTasks()
    }

    private fun getTasks() {
        viewModelScope.launch{
            Log.i("taskdate", "${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)}-${cal.get(Calendar.YEAR)}")
            rep.getTasksBetweenStartOfDayAnd24Hours(selectedGroup, cal).collect {
                when(it){
                    is ResultState.Success -> {
                        _tasks.value = it.data
                        Log.d("data", "gettasks succes: $_tasks")}
                    else -> {
                        Log.d("fail", "gettasks failed")
                        Unit}
                }
            }
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            rep.checkTask(task, selectedGroup).collect {
            }
        }
    }
}