package com.heppihome.viewmodels.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.ShopItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeInventoryViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel()
{
    private var group = Group()
    private val _inventory = MutableStateFlow(emptyList<ShopItem>())
    val inventory = _inventory.asStateFlow()

    private fun refreshInventory() {
        viewModelScope.launch {
            rep.getAllInventoryItems(group.id).collect {
                if (it is ResultState.Success) _inventory.value = it.data
            }
        }
    }

    fun cashInReward(si : ShopItem) {
        viewModelScope.launch {
            rep.cashInInventoryItem(group.id, si).collect { }
        }
        refreshInventory()
    }

    fun setGroup(g : Group) {
        group = g
        refreshInventory()
    }
}