package com.heppihome.viewmodels

import androidx.lifecycle.ViewModel
import com.heppihome.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeOverviewViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
    private val cal = GregorianCalendar()
}