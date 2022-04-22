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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class HomeTasksViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val testUsers = listOf(
        User("Pieter-Jan", "pjiscool@gmail.com"),
        User("Marieke", "EmeraldFire@gmail.com"),
        User("Alfonso", "muisjeinhethuisje@gmail.com")
    )
    private var testGroup : Group = Group("test groep", "dit is een groep dus", testUsers, "KXuXm9sRW43maz0Dbi2u")
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    val group = testGroup
    val tasks : StateFlow<List<Task>> = _tasks


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

    // TODO Store Group in local Room DataBase for persistence between navigation.
    // This function will then retrieve the current active Group.
    fun getGroup(id : String) : Group {
        return testGroup
    }

    fun onChangeGroup(group : Group) {
        rep.removeListeners()
        testGroup = group
        rep.registerTaskSnapshotListener(this::taskListener, testGroup)
    }

    fun onGoBack() {
        rep.removeListeners()
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