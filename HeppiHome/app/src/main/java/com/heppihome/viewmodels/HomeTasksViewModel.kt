package com.heppihome.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObjects
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeTasksViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val testUser : User = User("test", "test@gmail.com")
    private var testGroup : Group = Group("test", "test", listOf(testUser), "MMfMgNsL4ywptNugxRVi")


    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _loadingPosted = MutableStateFlow(false)

    val tasks : StateFlow<List<Task>> = _tasks
    val loadingPosted : StateFlow<Boolean> = _loadingPosted

    private fun taskListener(value : QuerySnapshot?, ex : FirebaseFirestoreException?) {
        if (ex != null) {
            Log.w("HomeMainViewModel", "Listen failed.", ex)
            return
        }
        if (value == null) {
            return
        }
        val newList = value.toObjects(Task::class.java)
        _tasks.value = newList
    }

    init {
        rep.registerTaskSnapshotListener(this::taskListener, testGroup)
    }

    fun onChangeGroup(group : Group) {
        rep.removeListeners()
        testGroup = group
        rep.registerTaskSnapshotListener(this::taskListener, testGroup)
    }

    fun toggleTask(t : Task) {
        viewModelScope.launch {
            rep.checkTask(t, testGroup).collect {
            }
        }
    }

    fun addTask(t : Task , g : Group = testGroup) {
        viewModelScope.launch {
            rep.addTask(t, g).collect { s ->
            }
        }
    }
}