package com.heppihome.data

import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObjects
import com.heppihome.BuildConfig
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {

    private lateinit var db : FirebaseFirestore;
    private val useEmulators : Boolean = BuildConfig.DEBUG
    private val COLLECTION_TASKS = "tasks"
    private val COLLECTION_GROUPS = "groups"
    private val COLLECTION_USERS = "users"

    private val listeners : MutableList<ListenerRegistration> = mutableListOf()

    private lateinit var groupDoc : CollectionReference
    private lateinit var userDoc : CollectionReference

    init {
        db = FirebaseFirestore.getInstance()
        if (useEmulators) {
            db.useEmulator("10.0.2.2", 8080)
        }
        groupDoc = db.collection(COLLECTION_GROUPS)
        userDoc = db.collection(COLLECTION_USERS)
    }

    // This gets all the groups
    suspend fun getAllGroups() : List<Group> {
        return groupDoc.get().await().toObjects(Group::class.java)
    }

    // This adds a group
    fun addGroup(group : Group) : Flow<ResultState<DocumentReference>> =
        flow {
            emit(ResultState.loading())

            val groupRef = groupDoc.document()
            groupRef.set(Group(group.name, group.description, group.users, groupRef.id)).await()

            emit(ResultState.success(groupRef))
        }.catch {
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    // Register a listener to changes to tasks in a certain group
    fun registerTaskSnapshotListener(listener : (QuerySnapshot?, FirebaseFirestoreException?) -> Unit, group : Group) {
        listeners.add(groupDoc.document(group.id).collection(COLLECTION_TASKS).addSnapshotListener(listener))
    }

    // Remove all listeners
    fun removeListeners() {
        for (l in listeners) {
            l.remove()
        }
        listeners.clear()
    }

    // Get all tasks for a certain group
    fun getAllTasks(user : User, group : Group) : Flow<ResultState<List<Task>>> =
        flow<ResultState<List<Task>>> {
            //emit loading state while fetching the content
            emit(ResultState.loading())

            val taskDoc = groupDoc.document(group.id).collection(COLLECTION_TASKS)

            val snapshot = taskDoc.get().await()
            val tasks = snapshot.toObjects<Task>()

            //emit data
            emit(ResultState.success(tasks))

        }.catch {
            // if exception
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    // This should get all tasks for all groups
    fun getAllTasks(user : User) : Flow<ResultState<List<Task>>> =
        // Does not work yet, because we need query index for the collection group

        flow<ResultState<List<Task>>> {
            //emit loading state while fetching the content
            emit(ResultState.loading())

            val taskDoc = db.collectionGroup(COLLECTION_TASKS).whereArrayContains("users", user.email)

            val snapshot = taskDoc.get().await()
            val tasks = snapshot.toObjects(Task::class.java)

            //emit data
            emit(ResultState.success(tasks))

        }.catch {
            // if exception
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    // This adds a task to a certain group
    fun addTask(task : Task, group: Group) : Flow<ResultState<DocumentReference>> =
        flow {
            //emit loading state while fetching the content
            emit(ResultState.loading())

            val taskRef = groupDoc.document(group.id).collection(COLLECTION_TASKS).document()
            taskRef.set(Task(task.text, task.done,task.deadline, task.users, taskRef.id)).await()

            //emit data
            emit(ResultState.success(taskRef))

        }.catch {
            // if exception
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    // This updates a task in a certain group's entry 'done' field. Changes true to false and false to true
    fun checkTask(task : Task, group : Group) : Flow<ResultState<DocumentReference>> =
        flow {
            emit(ResultState.loading())
            val taskRef = groupDoc.document(group.id).collection(COLLECTION_TASKS).document(task.id)
                taskRef.update("done", !task.done).await()
            emit(ResultState.success(taskRef))
        }.catch {
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

}