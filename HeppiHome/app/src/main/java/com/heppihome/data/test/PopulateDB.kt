package com.heppihome.data.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.HomeGroupViewModel
import com.heppihome.viewmodels.HomeTasksViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class PopulateDB constructor(private val viewmTask : HomeTasksViewModel,
private val viewmGroup : HomeGroupViewModel) {

    private val testGroups = listOf(
        Group("test groep", "dit is een groep dus", listOf(), "KXuXm9sRW43maz0Dbi2u"),
        Group("Tweede groep", "dit is ook een groep", listOf(), "qADb3QuTcFwlYk1yQBOj"),
        Group("groepie groep", "alweer een groep, gekte", listOf(), "TyEA0R9Jb1DPhsKBMRE0")
    )

    private val testTasks = listOf(
        Task("Zet de plantjes buiten", false, Timestamp(Date()), listOf()),
        Task("Doe eens gek", true, users = listOf()),
        Task("Dit is een mooie en nogal lange taak", false),
        Task("Nog meer taken", false, users = listOf()),
        Task("Sample Task", true, users = listOf())
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