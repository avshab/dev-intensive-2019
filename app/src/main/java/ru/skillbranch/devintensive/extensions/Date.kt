package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String="HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String {
    val pattern = if(this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time/DAY
    val day2 = date.time/DAY
    return day1 == day2
}


fun Date.add(value: Int, units: TimeUnits): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {

    val timeDiff = ((this.time - date.time)/ SECOND).toInt()

    val start = if (timeDiff > 0) {"через " } else { "" }
    val end = if (timeDiff < 0) {" назад" } else { "" }

    val humanDiff = when(abs(timeDiff)){
        in 0..1 -> "только что"
        in 1..45 -> "несколько секунд"
        in 45..75 -> "минуту"
        in 75..45*60 -> TimeUnits.MINUTE.plural(abs(timeDiff)/60)
        in 45*60..75*60 -> "час"
        in 75*60..22*3600 -> TimeUnits.HOUR.plural(abs(timeDiff)/(60*60))
        in 22*3600..26*3600 -> "день"
        in 26*3600..360*24*3600 -> TimeUnits.DAY.plural(abs(timeDiff)/(60*60*24))
        else -> { if(timeDiff > 0) {return "более чем через год"} else {return  "более года назад"}  }
    }

    return "$start$humanDiff$end"
}


enum class TimeUnits{
    SECOND{
        override fun getSelfInValidDeclension(type: NumericDeclensionType) = when(type) {
            NumericDeclensionType.FIRST -> "секунд"
            NumericDeclensionType.SECOND -> "секунду"
            NumericDeclensionType.THIRD -> "секунды"
        }
    },
    MINUTE{
        override fun getSelfInValidDeclension(type: NumericDeclensionType) = when(type) {
            NumericDeclensionType.FIRST -> "минут"
            NumericDeclensionType.SECOND -> "минуту"
            NumericDeclensionType.THIRD -> "минуты"
        }
    },
    HOUR{
        override fun getSelfInValidDeclension(type: NumericDeclensionType) = when(type) {
            NumericDeclensionType.FIRST -> "часов"
            NumericDeclensionType.SECOND -> "час"
            NumericDeclensionType.THIRD -> "часа"
        }
    },
    DAY{
        override fun getSelfInValidDeclension(type: NumericDeclensionType) = when(type) {
            NumericDeclensionType.FIRST -> "дней"
            NumericDeclensionType.SECOND -> "день"
            NumericDeclensionType.THIRD -> "дня"
        }
    };

    abstract fun getSelfInValidDeclension(type: NumericDeclensionType) : String

    fun plural(value: Int) : String{
        this.getSelfInValidDeclension(value.getDeclensionType())

        return "$value ${this.getSelfInValidDeclension(value.getDeclensionType())}"
    }
}


enum class NumericDeclensionType {
    FIRST,
    SECOND,
    THIRD;
}

fun Int.getDeclensionType() : NumericDeclensionType {
    if(this==0 || this in 5..20) return NumericDeclensionType.FIRST
    return when(this%10) {
        0, in 5..9 -> NumericDeclensionType.FIRST
        1 -> NumericDeclensionType.SECOND
        else -> NumericDeclensionType.THIRD
    }
}
