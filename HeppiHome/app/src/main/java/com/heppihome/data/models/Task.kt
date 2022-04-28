package com.heppihome.data.models

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser

data class Task(val text: String = "default",
                val done : Boolean = false,
                val deadline : Timestamp = Timestamp.now(),
                val users : List<String> = listOf(),
                val id : String = "default")