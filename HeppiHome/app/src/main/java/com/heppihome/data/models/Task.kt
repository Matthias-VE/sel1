package com.heppihome.data.models

import com.google.firebase.Timestamp

data class Task(val text: String = "default",
                val done : Boolean = false,
                val deadline : Timestamp = Timestamp.now(),
                val users : List<User> = listOf(),
                val id : String = "default")