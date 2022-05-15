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
class DateTasksViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    var cal = GregorianCalendar()

    private val _date = MutableStateFlow(
        DateUtil.formatDate(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.YEAR)
        )
    )

    val date = _date.asStateFlow()

    private var selectedGroup = Group()

    private val _tasks : MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())

    val tasks : StateFlow<List<Task>> = _tasks

    fun setCalendar(cal : GregorianCalendar){
        this.cal = cal
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            rep.checkTask(task, selectedGroup).collect {
            }
        }
    }

    fun getTasks() {
        Log.i("date", "${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)}-${cal.get(Calendar.YEAR)}")
        viewModelScope.launch{
            rep.getTasksBetweenStartOfDayAnd24Hours(selectedGroup, cal).collect {
                when(it){
                    is ResultState.Success -> {
                        _tasks.value = it.data
                        Log.d("data", "gettasks succes: ${_tasks.value}")}
                    else -> {
                        Log.d("fail", "gettasks failed")
                        Unit}
                }
            }
        }
    }
}