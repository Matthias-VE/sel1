package com.heppihome.data

import androidx.compose.ui.res.stringResource
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObjects
import com.google.type.Date
import com.heppihome.data.models.*
import com.heppihome.data.models.Constants.COLLECTION_GROUPS
import com.heppihome.data.models.Constants.COLLECTION_INVITES
import com.heppihome.data.models.Constants.COLLECTION_TASKS
import com.heppihome.data.models.Constants.COLLECTION_USERS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import com.heppihome.R
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
        user : User,
        start : Timestamp,
        end : Timestamp
    ) {
        listeners.add(groupDoc.document(group.id).collection(COLLECTION_TASKS)
            .whereArrayContains(COLLECTION_USERS, user.id)
            .whereGreaterThan("deadline", start)
            .whereLessThan("deadline", end)
            .addSnapshotListener(listener))
    }

    fun isAnonymousUser() : Boolean {
        return (firebaseAuth.currentUser == null || firebaseAuth.currentUser!!.isAnonymous)
    }

    fun getUser() : FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // This gets all the groups for a user
    suspend fun getAllGroups(user : User) : List<Group> {
        return groupDoc.whereArrayContains(COLLECTION_USERS, user.id).
        get().await().toObjects(Group::class.java)
    }

    suspend fun getUsersForIds(ids : List<String>) : List<User>{
        return userDoc.whereIn("id", ids).get().await().toObjects(User::class.java)
    }

    suspend fun getUserForId(id : String) : User? {
        return userDoc.document(id).get().await().toObject(User::class.java)
    }

    suspend fun addUser(u : User) {
        val userRef = userDoc.document(u.id)
        userRef.set(u).await()
    }

    suspend fun getAllInvites(user : User) : List<Invite>{
        return userDoc.document(user.id).collection(COLLECTION_INVITES)
            .get().await().toObjects(Invite::class.java)
    }

    suspend fun addInviteToPerson(emailTo : String,emailFrom : String, g : Group) : Boolean {
        val userRef = userDoc.whereEqualTo("email", emailTo).get().await()
        if (userRef.isEmpty) return false
        val users = userRef.toObjects(User::class.java)

        // email is unique per user so list should always have at most 1 element.

        val inviteRef = userDoc.document(users[0].id).collection(COLLECTION_INVITES).document()
        inviteRef.set(Invite(g.id, emailFrom, inviteRef.id))

        return true
    }

    suspend fun addPersonToGroupId(u : User, groupId : String) {
        groupDoc.document(groupId).update(COLLECTION_USERS, FieldValue.arrayUnion(u.id)).await()
    }

    fun removePersonFromGroupId( u : User, groupId: String) = flow {
        emit(ResultState.loading())
        groupDoc.document(groupId).update(COLLECTION_USERS, FieldValue.arrayRemove(u.id)).await()
        emit(ResultState.success("Successfully left group"))
    }.catch {
        emit(ResultState.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun removeInviteFromPerson(user: User, invite: Invite) {
        userDoc.document(user.id).collection(COLLECTION_INVITES)
            .document(invite.inviteId).delete().await()
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


    fun deleteGroup(group : Group) : Flow<ResultState<String>> =
        flow {
            emit(ResultState.loading())

            val groupRef = groupDoc.document(group.id)
            groupRef.delete().await()
            emit(ResultState.success("Group deleted successfully"))
        }.catch {
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun editGroup(group : Group, newName : String, newDesc : String) : Flow<ResultState<String>> {
        return flow {
            emit(ResultState.loading())
            groupDoc.document(group.id).update("name", newName, "description", newDesc).await()
            emit(ResultState.success("Group edited successfully"))
        }.catch{
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }


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

    fun getTasksBetweenStartAndEnd(user : User, g : Group, start : Timestamp, end : Timestamp) =
        flow {
            emit(ResultState.loading<List<Task>>())
            emit(ResultState.success(
                groupDoc.document(g.id).collection(COLLECTION_TASKS)
                .whereArrayContains(COLLECTION_USERS, user.id)
                .whereGreaterThan("deadline", start)
                .whereLessThan("deadline", end).get().await().toObjects(Task::class.java))
            )
        }.catch {
            emit(ResultState.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

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