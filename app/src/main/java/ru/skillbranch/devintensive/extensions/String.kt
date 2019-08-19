package ru.skillbranch.devintensive.extensions

import java.sql.Struct

fun TimeUnits.plural(value: Int) : String {

    var name = ""
    if(this.equals(TimeUnits.SECOND)) {
        name = when(value) {
            0, in 5..20, in 25..30, in 35..40, in 45..50, in 55..60 -> "секунд"
            1, 21, 31, 41, 51 -> "секунду"
            in 2..4, in 22..24, in 32..34, in 42..44, in 52..54 -> "секунды"
            else -> ""
        }
    }

    if(this.equals(TimeUnits.MINUTE)) {
        name = when(value) {
            0, in 5..20, in 25..30, in 35..40, in 45..50, in 55..60 -> "минут"
            1, 21, 31, 41, 51 -> "минуту"
            in 2..4, in 22..24, in 32..34, in 42..44, in 52..54 -> "минуты"
            else -> ""
        }
    }

    if(this.equals(TimeUnits.HOUR)) {
        name = when(value) {
            1, 21 -> "час"
            in 2..4, in 22..24 -> "часа"
            0, in 5..20 -> "часов"
            else -> ""
        }
    }

    if(this.equals(TimeUnits.DAY)) {
        name = when(value) {
            0, in 5..20, in 25..30, in 35..40, in 45..50, in 55..60  -> "дней"
            1, 21, 31, 41, 51 -> "день"
            in 2..4, in 22..24, in 32..34, in 42..44, in 52..54 -> "дня"
            else -> ""
        }
    }

    return "$value $name"
}


fun String.truncate(count: Int = 16) : String {
    if(count <= 0) return ""
    val trimStr = trimEnd()
    return when(trimStr.length) {
        in 0..count -> trimStr
        else -> trimStr.substring(0,count).trimEnd() + "..."
    }
}

fun String.stripHtml(): String {
    return this.substringBefore("</p>")
            .substringAfterLast(">")
            .replace("&", "")
            .replace("<", "")
            .replace(">", "")
            .replace("\'", "")
            .replace("\"", "")
            .split(" ")
            .filter { !it.isEmpty() }.joinToString(" ")
}
