package com.heppihome.data.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.google.firebase.Timestamp
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import com.heppihome.viewmodels.tasks.HomeTasksViewModel
import java.util.*

class PopulateDB constructor(private val viewmTask : HomeTasksViewModel,
                             private val viewmGroup : HomeGroupViewModel
) {

    private val admin_id = "N8BRCYIwn2bTO52pANuONS3nvJk1"

    private val testGroups = listOf(
        Group("test groep", "dit is een groep dus", listOf(admin_id), "KXuXm9sRW43maz0Dbi2u"),
        Group("Tweede groep", "dit is ook een groep", listOf(admin_id), "qADb3QuTcFwlYk1yQBOj"),
        Group("groepie groep", "alweer een groep, gekte", listOf(admin_id), "TyEA0R9Jb1DPhsKBMRE0")
    )

    private val testTasks = listOf(
        Task("Zet de plantjes buiten", false, Timestamp(Date()), listOf(admin_id)),
        Task("Doe eens gek", true, users = listOf(admin_id)),
        Task("Dit is een mooie en nogal lange taak", false),
        Task("Nog meer taken", false, users = listOf(admin_id)),
        Task("Sample Task", true, users = listOf(admin_id))
    )

    @Composable
    fun Populate() {
            if (viewmGroup.groups.collectAsState().value.isEmpty()) {
                testGroups.forEach {
                    viewmGroup.addGroupWithId(it)
                }
                testGroups.forEach {
                    testTasks.forEach { t ->
                        viewmTask.addTask(t, it)
                    }
                }
            }

    }
}