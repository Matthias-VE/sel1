package com.heppihome.viewmodels

import androidx.lifecycle.ViewModel
import com.heppihome.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeSettingsViewModel @Inject constructor(private val rep : HomeRepository): ViewModel() {
}