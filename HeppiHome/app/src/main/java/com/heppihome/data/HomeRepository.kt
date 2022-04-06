package com.heppihome.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.heppihome.BuildConfig
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import com.heppihome.data.sources.test.Backend
import com.heppihome.data.sources.web.FirestoreDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val backend : Backend) {

    fun getTask() : Task = backend.getTask()

    fun updateTask(t : Task, t2 : Task) = backend.updateTask(t, t2)

    private val db = FirebaseFirestore.getInstance()
    private val useEmulators : Boolean = BuildConfig.DEBUG
    private val COLLECTION_TASKS = "tasks"

    private val taskDoc = db.collection(COLLECTION_TASKS)


    fun getAllTasks() : Flow<ResultState<List<Task>>> =
        flow<ResultState<List<Task>>> {
            //emit loading state while fetching the content
            emit(ResultState.loading())

            val snapshot = taskDoc.get().await()
            val tasks = snapshot.toObjects(Task::class.java)

            //emit data
            emit(ResultState.success(tasks))

        }.catch {
            // if exception
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    fun addTask(task : Task) =
        flow<ResultState<DocumentReference>> {
            //emit loading state while fetching the content
            emit(ResultState.loading())

            val taskRef = taskDoc.add(task).await()

            //emit data
            emit(ResultState.success(taskRef))

        }.catch {
            // if exception
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    suspend fun checkTask() {
        //TODO()
    }

    suspend fun UncheckTask() {
        //TODO()
    }

}