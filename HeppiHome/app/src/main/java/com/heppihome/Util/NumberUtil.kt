package com.heppihome.Util

object NumberUtil {

    fun verifyInput(s : String) : Int {
        var i = 0
        runCatching {
            i = Integer.parseInt(s)
        }
        return i
    }
}