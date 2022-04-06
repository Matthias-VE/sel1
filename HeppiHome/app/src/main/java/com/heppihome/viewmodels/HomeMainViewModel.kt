package com.heppihome.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.heppihome.data.HomeRepository
import com.heppihome.data.sources.test.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class HomeMainViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _tasks = MutableStateFlow(listOf(rep.getTask()))

    val tasks : StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask() {
        val m = mutableListOf<Task>(rep.getTask())
        m.addAll(_tasks.value)
        _tasks.value = m.toList()
    }

    fun toggleDone(t : Task) {
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