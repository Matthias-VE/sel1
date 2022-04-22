package com.heppihome.data.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.firebase.Timestamp
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.viewmodels.HomeGroupViewModel
import com.heppihome.viewmodels.HomeTasksViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class PopulateDB constructor(private val viewmTask : HomeTasksViewModel,
private val viewmGroup : HomeGroupViewModel) {
    private val testUsers = listOf(
        User("Pieter-Jan", "pjiscool@gmail.com"),
        User("Marieke", "EmeraldFire@gmail.com"),
        User("Alfonso", "muisjeinhethuisje@gmail.com")
    )

    private val testGroups = listOf(
        Group("test groep", "dit is een groep dus", testUsers, "KXuXm9sRW43maz0Dbi2u"),
        Group("Tweede groep", "dit is ook een groep", listOf(testUsers[1], testUsers[0]), "qADb3QuTcFwlYk1yQBOj"),
        Group("groepie groep", "alweer een groep, gekte", listOf(testUsers[2]), "TyEA0R9Jb1DPhsKBMRE0")
    )

    private val testTasks = listOf(
        Task("Zet de plantjes buiten", false, Timestamp(Date()), testUsers),
        Task("Doe eens gek", true, users = listOf(testUsers[2])),
        Task("Dit is een mooie en nogal lange taak", false, users = testUsers),
        Task("Nog meer taken", false, users= testUsers),
        Task("Sample Task", true, users = testUsers)
    )

    @Composable
    fun Populate() {
            if (viewmGroup.groups.collectAsState().value.isEmpty()) {
                testGroups.forEach {
                    viewmGroup.addGroupWithId(it)
                }
            }
            if (viewmTask.tasks.collectAsState().value.isEmpty()) {
                testGroups.forEach {
                    testTasks.forEach { t ->
                        viewmTask.addTask(t, it)
                    }
                }
            }
    }
}