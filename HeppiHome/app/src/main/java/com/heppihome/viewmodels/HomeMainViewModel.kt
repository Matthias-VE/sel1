package com.heppihome.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor (private val rep : HomeRepository) : ViewModel() {


    var selectedInvite : Invite = Invite(
        "default",
        "default",
        "default"
    )

    var toEditGroup : Group = Group()

    var calendar : GregorianCalendar = GregorianCalendar()
    
    var toEditShopItem : ShopItem = ShopItem()

}