package com.heppihome.viewmodels.tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeTasksViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _tasksTod = MutableStateFlow<List<Task>>(emptyList())
    private val _tasksTom = MutableStateFlow<List<Task>>(emptyList())
    private val _canNotResign = MutableStateFlow(false)

    val tasksToday : StateFlow<List<Task>> = _tasksTod

    val tasksTomorrow : StateFlow<List<Task>> = _tasksTom

    private var _expanded : MutableStateFlow<Boolean> = MutableStateFlow(false)
    var expanded : StateFlow<Boolean> = _expanded

    val canNotResign = _canNotResign.asStateFlow()
    val group = {rep.selectedGroup}
    val isAdmin = rep.isAdmin

    private fun taskListenerToday(value : QuerySnapshot?, ex : FirebaseFirestoreException?) {
        if (ex != null) {
            Log.w("HomeMainViewModel", "Listen failed.", ex)
            return
        }
        if (value == null) {
            return
        }
        val newList = value.toObjects(Task::class.java)
        _tasksTod.value = newList
    }

    private fun taskListenerTomorrow(value : QuerySnapshot?, ex : FirebaseFirestoreException?) {
        if (ex != null) {
            Log.w("HomeMainViewModel", "Listen failed.", ex)
            return
        }
        if (value == null) {
            return
        }
        val newList = value.toObjects(Task::class.java)
        _tasksTom.value = newList
    }

    fun startListeners() {
        rep.registerTodayTasksListenerForUserAndGroup(this::taskListenerToday)
        rep.registerTomorrowTasksListenerForUserAndGroup(this::taskListenerTomorrow)
    }

    // Return true for success and false for failed
    fun resignAsAdmin() : Boolean {
        // true if last admin, false if not
        val check = rep.checkLastAdmin()
        if (!check) {
            viewModelScope.launch {
                rep.removeSelfAdmin()
            }
            return true
        }
        return false
    }

    fun onGoBack() {
        rep.removeListeners()
    }

    fun toggleDropdownMenu() {
        _expanded.value = !_expanded.value
    }

    fun toggleTask(t : Task) {
        viewModelScope.launch {
            rep.checkTask(t).collect {
            }
        }
    }

}