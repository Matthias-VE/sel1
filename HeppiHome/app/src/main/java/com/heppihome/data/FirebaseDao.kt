package com.heppihome.data

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObjects
import com.google.type.Date
import com.heppihome.data.models.Constants.COLLECTION_GROUPS
import com.heppihome.data.models.Constants.COLLECTION_TASKS
import com.heppihome.data.models.Constants.COLLECTION_USERS
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.inject.Singleton

@Singleton
class FirebaseDao {
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance();
    private val useEmulators : Boolean = false

    private val listeners : MutableList<ListenerRegistration> = mutableListOf()

    private var groupDoc : CollectionReference
    private var userDoc : CollectionReference

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance();

    init {
        if (useEmulators) {
            db.useEmulator("10.0.2.2", 8080)
        }
        groupDoc = db.collection(COLLECTION_GROUPS)
        userDoc = db.collection(COLLECTION_USERS)
    }
    fun registerListenerForRecentTasks(
        listener : (QuerySnapshot?, FirebaseFirestoreException?) -> Unit,
        group : Group,
        user : User
    ) {
        val today = LocalDate.now().atStartOfDay()
        val tomorrow = today.plusDays(1)
        listeners.add(groupDoc.document(group.id).collection(COLLECTION_TASKS)
            .whereArrayContains(COLLECTION_USERS, user.id)
            //.whereGreaterThan("deadline", today)
            //.whereLessThan("deadline", tomorrow)
            .addSnapshotListener(listener))
    }

    fun isAnonymousUser() : Boolean {
        return (firebaseAuth.currentUser == null || firebaseAuth.currentUser!!.isAnonymous)
    }

    fun getUser() : FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // This gets all the groups
    suspend fun getAllGroups() : List<Group> {
        return groupDoc.get().await().toObjects(Group::class.java)
    }

    // This adds a group with an id set already
    fun addGroupWithId(group : Group) : Flow<ResultState<DocumentReference>> =
        flow {
            emit(ResultState.loading())
            val groupref = groupDoc.document(group.id)
            groupref.set(group).await()
            emit(ResultState.success(groupref))
        }.catch {
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    // This adds a group with no id set yet
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
    fun getAllTasks(user : FirebaseUser, group : Group) : Flow<ResultState<List<Task>>> =
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
    fun getAllTasks(user : FirebaseUser) : Flow<ResultState<List<Task>>> =
        // Does not work yet, because we need query index for the collection group

        flow<ResultState<List<Task>>> {
            //emit loading state while fetching the content
            emit(ResultState.loading())

            val taskDoc = db.collectionGroup(COLLECTION_TASKS).whereArrayContains("users", user.email!!)

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