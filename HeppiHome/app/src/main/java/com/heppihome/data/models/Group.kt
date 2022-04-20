package com.heppihome.data.models

data class Group(val name : String = "default",
                 val description : String = "default",
                 val users : List<User> = listOf(),
                 val id: String = "default")