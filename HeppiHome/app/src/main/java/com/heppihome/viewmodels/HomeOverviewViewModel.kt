package com.heppihome.viewmodels

import android.util.Log
import android.widget.CalendarView
import androidx.lifecycle.ViewModel
import com.heppihome.Util.DateUtil
import com.heppihome.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeOverviewViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
    var cal = GregorianCalendar()

    private val _date = MutableStateFlow(
        DateUtil.formatDate(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.YEAR)
        )
    )

    fun resetDate(){
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
    }
}