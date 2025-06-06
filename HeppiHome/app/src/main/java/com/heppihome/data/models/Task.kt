package com.heppihome.data.models

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDate
import java.util.*

data class Task(val text: String = "default",
                val done : Boolean = false,
                val deadline : Timestamp = Timestamp.now(),
                val users : List<String> = listOf(),
                val points : Int = 0,
                val id : String = "default")