package com.heppihome.viewmodels.groups

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeGroupViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _groups : MutableStateFlow<List<Group>> = MutableStateFlow(emptyList())
    val groups : StateFlow<List<Group>> = _groups

    private var _toastMessage : MutableStateFlow<String> = MutableStateFlow("")
    var toastMessage : StateFlow<String> = _toastMessage

    private var _expanded : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var expanded : StateFlow<Boolean> = _expanded

    private val _status = MutableStateFlow<ResultState<String>>(ResultState.waiting())
    val status = _status.asStateFlow()

    private var _hasInvites : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var hasInvites : StateFlow<Boolean> = _hasInvites

    init {
        refreshInvites()
        refreshGroups()
    }

    fun setGroup(g : Group) {
        rep.changeGroup(g)
    }

    fun refreshInvites() {
        viewModelScope.launch {
            val temp = rep.getAllInvites()
            _hasInvites.value = temp.isNotEmpty()
        }
    }

    fun checkInvites() : Boolean {
        viewModelScope.launch {
            refreshInvites()
        }
        return _hasInvites.value
    }

    fun setToast(newToast : String, context: Context) {
        _toastMessage.value = newToast
        Toast.makeText(context, _toastMessage.value, Toast.LENGTH_LONG).show()
    }

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

    fun leaveGroup(g : Group, context: Context) {
        viewModelScope.launch {
            rep.leaveGroup(g).collect {
                _status.value = it
                when(it) {
                    is ResultState.Success -> setToast("Left group succesfully", context) // Do something when succes
                    is ResultState.Loading -> setToast("Leaving group...", context) // Do something while loading
                    is ResultState.Failed -> setToast("Something went wrong\nPlease try again", context) // Do something when failed
                }
            }
            _groups.value = rep.getAllGroups()
        }
    }

}