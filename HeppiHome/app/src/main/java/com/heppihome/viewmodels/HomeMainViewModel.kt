package com.heppihome.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor (private val rep : HomeRepository) : ViewModel() {

    var selectedGroup : Group = Group(
        Constants.NOT_SELECTED,
        Constants.NOT_SELECTED,
        id = Constants.NOT_SELECTED
    )

    var selectedInvite : Invite = Invite(
        "default",
        "default",
        "default"
    )

    var toEditGroup : Group = Group()

    var calendar : GregorianCalendar = GregorianCalendar()
}