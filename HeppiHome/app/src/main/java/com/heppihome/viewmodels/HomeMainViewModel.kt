package com.heppihome.viewmodels

import androidx.lifecycle.ViewModel
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

    var fromTasks = true

    var selectedTask : Task = Task()

    var toEditGroup : Group = Group()

    var calendar : GregorianCalendar = GregorianCalendar()
    
    var toEditShopItem : ShopItem = ShopItem()

}