package com.heppihome.data.models

data class Points(
    // Will also be used as id since no user can have multiple points for the same group
    val groupId : String = "default",
    val points : Int = 0
)