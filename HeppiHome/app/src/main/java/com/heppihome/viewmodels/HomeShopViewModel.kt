package com.heppihome.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeShopViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _points = MutableStateFlow<Shop>(Shop("",0))
    val points = _points.asStateFlow()

    private val _shopItems = MutableStateFlow<List<ShopItem>>(emptyList())
    val shopItems = _shopItems.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin = _isAdmin.asStateFlow()

    private val _buySuccess = MutableStateFlow(Pair(false, false))
    val buySuccess = _buySuccess.asStateFlow()

    private var group : Group = Group()


    fun refreshItems() {
        viewModelScope.launch {
            rep.getShopItems(group.id).collect {
                if (it is ResultState.Success) {
                    _shopItems.value = it.data
                }
            }
        }
    }

    fun resetSuccess() {
        _buySuccess.value = Pair(false, false)
    }

    fun buyItem(si : ShopItem) : Boolean {
        return if (si.points <= _points.value.points) {
            viewModelScope.launch{
                rep.updatePoints(group, -si.points).collect { if (it is ResultState.Success) _buySuccess.value = Pair(true, false) }
                rep.addItemToInventory(group.id, si).collect { if (it is ResultState.Success) _buySuccess.value = Pair(true, true) }
            }
            true
        } else false
    }

    private fun pointsListener(value : DocumentSnapshot?, ex : FirebaseFirestoreException?) {
        if (ex != null) {
            Log.w("HomeShopViewModel", "Listen failed.", ex)
            return
        }
        if (value == null) {
            return
        }
        val newPoints = value.toObject(Shop::class.java)
        if (newPoints != null) {
            _points.value = newPoints
        } else return
    }

    fun setGroup(g : Group) {
        group = g
        _isAdmin.value = rep.isAdmin(g)
        rep.registerPointsListener(this::pointsListener, g.id)
    }

}