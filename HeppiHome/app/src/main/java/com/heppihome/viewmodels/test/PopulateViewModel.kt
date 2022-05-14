package com.heppihome.viewmodels.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Shop
import com.heppihome.data.models.ShopItem
import com.heppihome.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopulateViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
    fun insertGroups( l : List<Group>) {
        viewModelScope.launch {
            for (g in l) {
                rep.addGroupWithId(g).collect {  }
            }
        }
    }

    fun insertPoints(l : List<Shop>, uid : String) {
        viewModelScope.launch {
            for (s in l) {
                rep.updatePointsWithUser(uid, s.groupId, s.points).collect {  }
            }
        }
    }

    fun insertTasks(l : List<Task>, g : Group) {
        viewModelScope.launch {
            for (t in l) {
                rep.addTask(t, g).collect {  }
            }
        }
    }

    fun insertShopItems(l : List<ShopItem>, g : Group) {
        viewModelScope.launch {
            for (i in l) {
                rep.addItemToShop(g.id, i).collect {  }
            }
        }
    }
}