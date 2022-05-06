package com.heppihome.viewmodels.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeGroupViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _groups : MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups : StateFlow<List<Group>> = _groups

    private var _expanded : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var expanded : StateFlow<Boolean> = _expanded

    fun expandGroupMenu() {
        viewModelScope.launch {
            _expanded.value = !_expanded.value
        }
    }

    fun refreshGroups() {
        viewModelScope.launch {
            _groups.value = rep.getAllGroups()
        }
    }

    fun addGroup(g : Group) {
        viewModelScope.launch {
            rep.addGroup(g).collect { }
        }
    }

    fun addGroupWithId(g : Group) {
        viewModelScope.launch {
            rep.addGroupWithId(g).collect {}
        }
    }

    fun deleteGroup(g: Group) {
        viewModelScope.launch {
            //fill in removegroup

        }
    }
}