package com.heppihome.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class HomeMainViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _tasks = MutableStateFlow(listOf(rep.getTask()))
    private val _loadingPosted = MutableStateFlow(false)

    val tasks : StateFlow<List<Task>> = _tasks.asStateFlow()
    val loadingPosted : StateFlow<Boolean> = _loadingPosted.asStateFlow()

    val allTasks : StateFlow<ResultState<List<Task>>> = rep.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, ResultState.loading())

    suspend fun addTask(t : Task) {
        rep.addTask(t).collect { s ->
            _loadingPosted.value = s is ResultState.Loading
        }
    }

    fun addTaskTest() {
        val m = mutableListOf<Task>(rep.getTask())
        m.addAll(_tasks.value)
        _tasks.value = m.toList()
    }

    fun toggleDoneTest(t : Task) {
        val newt : Task
        if (t.done) {
            newt = Task(t.text, false)
            rep.updateTask(t, newt)
        } else {
            newt = Task(t.text, true)
            rep.updateTask(t, newt)
        }
        val m = mutableListOf<Task>()
        m.addAll(_tasks.value)
        val i = m.indexOf(t)
        m.removeAt(i)
        m.add(i, newt)
        _tasks.value = m.toList()
    }
}