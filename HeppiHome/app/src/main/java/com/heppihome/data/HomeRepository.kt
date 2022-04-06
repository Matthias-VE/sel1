package com.heppihome.data

import com.google.firebase.firestore.FirebaseFirestore
import com.heppihome.BuildConfig
import com.heppihome.data.sources.test.Backend
import com.heppihome.data.sources.test.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val backend : Backend) {

    private val useEmulators : Boolean = BuildConfig.DEBUG

    fun getTask() : Task = backend.getTask()

    fun updateTask(t : Task, t2 : Task) = backend.updateTask(t, t2)

}