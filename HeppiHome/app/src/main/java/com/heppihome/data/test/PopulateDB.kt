package com.heppihome.data.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.google.firebase.Timestamp
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import com.heppihome.viewmodels.HomeTasksViewModel
import java.util.*

class PopulateDB constructor(private val viewmTask : HomeTasksViewModel,
private val viewmGroup : HomeGroupViewModel
) {

    private val testGroups = listOf(
        Group("test groep", "dit is een groep dus", listOf("2zqXUOKV91fdd4HgTwe0IHWe3NB2"), "KXuXm9sRW43maz0Dbi2u"),
        Group("Tweede groep", "dit is ook een groep", listOf("2zqXUOKV91fdd4HgTwe0IHWe3NB2"), "qADb3QuTcFwlYk1yQBOj"),
        Group("groepie groep", "alweer een groep, gekte", listOf("2zqXUOKV91fdd4HgTwe0IHWe3NB2"), "TyEA0R9Jb1DPhsKBMRE0")
    )

    private val testTasks = listOf(
        Task("Zet de plantjes buiten", false, Timestamp(Date()), listOf("2zqXUOKV91fdd4HgTwe0IHWe3NB2")),
        Task("Doe eens gek", true, users = listOf("2zqXUOKV91fdd4HgTwe0IHWe3NB2")),
        Task("Dit is een mooie en nogal lange taak", false),
        Task("Nog meer taken", false, users = listOf("2zqXUOKV91fdd4HgTwe0IHWe3NB2")),
        Task("Sample Task", true, users = listOf("2zqXUOKV91fdd4HgTwe0IHWe3NB2"))
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