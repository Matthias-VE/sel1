package com.heppihome.data.models

import com.google.firebase.auth.FirebaseUser

data class Group(val name : String = "default",
                 val description : String = "default",
                 val users : List<String> = listOf(),
                 val admins : List<String> = listOf(),
                 val id: String = "default")