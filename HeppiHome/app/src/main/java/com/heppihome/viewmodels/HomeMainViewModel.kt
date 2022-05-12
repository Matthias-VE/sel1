package com.heppihome.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Constants
import com.heppihome.data.models.Group
import com.heppihome.data.models.Invite
import com.heppihome.data.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor (private val rep : HomeRepository) : ViewModel() {

    var selectedGroup : Group = Group(
        Constants.ALL_TASKS,
        Constants.ALL_TASKS,
        id = Constants.ALL_TASKS
    )

    var selectedInvite : Invite = Invite(
        "default",
        "default",
        "default"
    )

    var toEditGroup : Group = Group()
}