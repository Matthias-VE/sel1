package com.heppihome.Util

object DateUtil {
    fun formatHours(hour: Int, minutes: Int): String {
        return if (hour < 10 && minutes < 10) {
            "0$hour:0$minutes"
        } else if (hour < 10) {
            "0$hour:$minutes"
        } else if (minutes < 10) {
            "$hour:0$minutes"
        } else {
            "$hour:$minutes"
        }
    }

    fun formatDate(day: Int, month: Int, year: Int): String {
        val acMonth = month + 1
        return if (day < 10 && acMonth < 10) {
            "$year-0$acMonth-0$day"
        } else if (day < 10) {
            "$year-$acMonth-0$day"
        } else if (month < 10) {
            "$year-0$acMonth-$day"
        } else {
            "$year-$acMonth-$day"
        }
    }
}