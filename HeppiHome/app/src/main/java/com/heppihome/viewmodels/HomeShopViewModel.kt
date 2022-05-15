package com.heppihome.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Points
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeShopViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private val _points = MutableStateFlow<Points>(Points("",0))
    val points = _points.asStateFlow()

    private fun pointsListener(value : DocumentSnapshot?, ex : FirebaseFirestoreException?) {
        if (ex != null) {
            Log.w("HomeShopViewModel", "Listen failed.", ex)
            return
        }
        if (value == null) {
            return
        }
        val newPoints = value.toObject(Points::class.java)
        if (newPoints != null) {
            _points.value = newPoints
        } else return
    }

    fun setGroup(g : Group) {
        rep.registerPointsListener(this::pointsListener, g.id)
    }

}