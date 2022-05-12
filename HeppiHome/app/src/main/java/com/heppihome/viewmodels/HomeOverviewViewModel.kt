package com.heppihome.viewmodels

import android.widget.CalendarView
import androidx.lifecycle.ViewModel
import com.heppihome.Util.DateUtil
import com.heppihome.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeOverviewViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
    private val cal = GregorianCalendar()

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
    }
}