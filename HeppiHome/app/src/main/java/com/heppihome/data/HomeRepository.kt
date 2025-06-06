package com.heppihome.data

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObjects
import com.heppihome.BuildConfig
import com.heppihome.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val fdao : FirebaseDao) {

    lateinit var user : User

    var isLoggedIn = false

    var selectedGroup : Group = Group()
        private set(value) {field = value; _isAdmin.value = user.id in value.admins; _admins.value = value.admins}

    private val _admins = MutableStateFlow(emptyList<String>())
    val admins = _admins.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin = _isAdmin.asStateFlow()

    private fun groupListener(value : DocumentSnapshot?, ex : FirebaseFirestoreException?)
    {
        if (ex != null) {
            Log.w("HomeMainViewModel", "Listen failed.", ex)
            return
        }
        if (value == null) {
            return
        }
        val newGroup = value.toObject(Group::class.java)
        if (newGroup != null) {
            selectedGroup = newGroup
        } else return
    }

    fun changeGroup(g : Group) {
        removeListeners()
        selectedGroup = g
        registerSelectedGroupListener(this::groupListener, g.id)
    }

    init {
        val u = getUser()
        if (u != null) {
            user = fuToUser(u)
            isLoggedIn = true
        }
    }

    fun registerSelectedGroupListener(listener: (DocumentSnapshot?, FirebaseFirestoreException?) -> Unit, gid : String) {
        fdao.registerGroupListener(listener, gid)
    }

    private fun fuToUser(fu : FirebaseUser) : User {
        var name = "default"
        var email = "default"
        fu.displayName?.let { name = it }
        fu.email?.let { email = it }
        return User(name, email, fu.uid)
    }

    fun isAnonymousUser() : Boolean {
        return fdao.isAnonymousUser()
    }

    fun getUser() : FirebaseUser? {
        return fdao.getUser()
    }

    // This gets all the groups
    suspend fun getAllGroups() : List<Group> {
        return fdao.getAllGroups(user)
    }

    suspend fun getAllUsersInGroup(l : List<String>) : List<User> {
        val list = mutableListOf<User>()
        for (id in l) {
            list.add(getUserFromId(id))
        }
        return list
    }

    suspend fun getUserFromId(id : String) : User {
        val u = fdao.getUserForId(id)
        return if (u == null) {
            Log.i("HomeRepo", "User does not exist")
            User()
        } else {
            u
        }
    }

    suspend fun saveUser(u : User) {
        fdao.addUser(u)
    }

    suspend fun sendInviteTo(emailTo : String, g : Group = selectedGroup) : Boolean {
        return fdao.addInviteToPerson(emailTo,user.email, g)
    }

    suspend fun acceptInvite(inv : Invite) {
        fdao.addPersonToGroupId(user, inv.groupId)
        fdao.removeInviteFromPerson(user, inv)
    }

    suspend fun declineInvite(inv : Invite) {
        fdao.removeInviteFromPerson(user, inv)
    }

    suspend fun getAllInvites() : List<Invite> {
        return fdao.getAllInvites(user)
    }

    // This adds a group with an id set already
    fun addGroupWithId(group : Group) : Flow<ResultState<DocumentReference>> =
        fdao.addGroupWithId(group)

    // This adds a group with no id set yet
    fun addGroup(group : Group) : Flow<ResultState<DocumentReference>> =
        fdao.addGroup(group)

    fun editGroup(group : Group , newName : String, newDesc : String) : Flow<ResultState<String>> {
        var nameCheck = newName.trim()
        var descCheck = newDesc.trim()
        if (nameCheck.isEmpty() && descCheck.isEmpty()) {
            return flow {emit(ResultState.failed("name and desc are empty"))}
        }
        if (nameCheck.isEmpty()) nameCheck = group.name
        if (descCheck.isEmpty()) descCheck = group.description
        return fdao.editGroup(group, nameCheck, descCheck)
    }
    // Delete group from existence
    fun deleteGroup(group : Group = selectedGroup) : Flow<ResultState<String>> =
        fdao.deleteGroup(group)

    // Remove current user from group
    fun leaveGroup(group : Group = selectedGroup) : Flow<ResultState<String>> =
        fdao.removePersonFromGroupId(user, group.id)

    fun addAdminToGroup(otherUser : User, groupId : String = selectedGroup.id) =
        fdao.makeOtherUserAdmin(otherUser, groupId)

    fun checkLastAdmin() : Boolean{
        return selectedGroup.admins.size <= 1
    }


    suspend fun removeSelfAdmin(groupId: String = selectedGroup.id) {
        fdao.removeUserFromAdmin(user, groupId).collect { }
    }


    fun registerTodayTasksListenerForUserAndGroup(listener: (QuerySnapshot?, FirebaseFirestoreException?) -> Unit,
                                                  group : Group = selectedGroup) {
        val cal = GregorianCalendar()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        val today = Timestamp(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = Timestamp(cal.time)
        fdao.registerListenerForRecentTasks(listener, group, user, today, tomorrow)
    }

    fun registerTomorrowTasksListenerForUserAndGroup(listener: (QuerySnapshot?, FirebaseFirestoreException?) -> Unit,
                                                     group : Group = selectedGroup) {
        val cal = GregorianCalendar()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = Timestamp(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, 1)
        val overmorrow = Timestamp(cal.time)
        fdao.registerListenerForRecentTasks(listener, group, user, tomorrow, overmorrow)
    }

    // Remove all listeners
    fun removeListeners() {
        fdao.removeListeners()
    }

    fun getTasksBetweenStartOfDayAnd24Hours(group : Group = selectedGroup, start : Calendar):
            Flow<ResultState<List<Task>>> {
        val copy = GregorianCalendar()
        copy.set(Calendar.HOUR_OF_DAY, 0)
        copy.set(Calendar.MINUTE, 0)
        copy.set(Calendar.SECOND, 0)
        copy.set(Calendar.YEAR, start.get(Calendar.YEAR))
        copy.set(Calendar.MONTH, start.get(Calendar.MONTH))
        copy.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH))
        val t1 = Timestamp(copy.time)
        copy.add(Calendar.DAY_OF_YEAR, 1)
        val t2 = Timestamp(copy.time)
        return fdao.getTasksBetweenStartAndEnd(user, group, t1, t2)
    }

    // This adds a task to a certain group
    fun addTask(task : Task, group: Group = selectedGroup) : Flow<ResultState<DocumentReference>> =
        fdao.addTask(task, group)

    fun updateTask(task : Task, group : Group = selectedGroup) : Flow<ResultState<String>> =
        fdao.updateTask(task, group)

    fun updatePoints(group : Group = selectedGroup, toBeAdded : Int) =
        fdao.updatePoints(user.id, group.id, toBeAdded)

    fun updatePointsWithUser(uid : String, groupId : String = selectedGroup.id, toBeAdded: Int) =
        fdao.updatePoints(uid, groupId, toBeAdded)

    // This updates a task in a certain group's entry 'done' field. Changes true to false and false to true
    fun checkTask(task : Task, group : Group = selectedGroup) : Flow<ResultState<String>> = flow<ResultState<String>> {
        val toAdd = !task.done
        emit(ResultState.loading())
        fdao.checkTask(task, group).collect { result ->
            if (result is ResultState.Success) {
                for (u in task.users) {
                    val user = fdao.getUserForId(u)
                    if (user != null) {
                        if (toAdd) fdao.updatePoints(user.id, group.id, task.points).collect { emit(it) }
                        else fdao.updatePoints(user.id, group.id, -task.points).collect { emit(it) }
                    }
                }
            }
        }
    }.catch {
        emit(ResultState.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getPoints(gid : String = selectedGroup.id) =
        fdao.getPoints(user, gid)

    fun registerPointsListener(
        listener : (DocumentSnapshot?, FirebaseFirestoreException?) -> Unit,
        gid : String = selectedGroup.id
    ) = fdao.addPointsListener(listener, user, gid)

    fun getShopItems(gid : String = selectedGroup.id) =
        fdao.getAllShopItems(gid)

    fun addItemToShop(groupId: String = selectedGroup.id, shopItem: ShopItem) =
        fdao.addItemToShop(groupId, shopItem)

    fun removeItemFromShop(gid : String = selectedGroup.id, item : ShopItem) =
        fdao.deleteShopItem(gid, item)

    fun editShopItemInShop(gid : String = selectedGroup.id, sitem : ShopItem) =
        fdao.updateShopItem(gid, sitem)

    fun getAllInventoryItems(gid: String = selectedGroup.id) =
        fdao.getAllInventoryItems(user.id, gid)

    fun addItemToInventory (gid : String = selectedGroup.id, sitem : ShopItem) =
        fdao.addShopItemToInventory(user, gid, sitem)

    fun cashInInventoryItem(gid : String = selectedGroup.id, sitem : ShopItem) =
        fdao.deleteShopItemFromInventory(user, gid, sitem)
}