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

fun Date.humanizeDiff(date:Date = Date()) =
    when (val diff = this.time - date.time) {
        in -1 * SECOND .. 1 * SECOND ->  "только что"
        in 1 * SECOND .. 45 * SECOND -> "несколько секунд вперед"
        in -45 * SECOND .. -1 * SECOND -> "несколько секунд назад"
        in 45 * SECOND .. 75 * SECOND -> "минуту вперед"
        in -75 * SECOND .. -45 * SECOND -> "минуту назад"
        in 75 * SECOND .. 45 * MINUTE -> "через ${TimeUnits.MINUTE.plural((diff /MINUTE).toInt())}"
        in -45 * MINUTE .. -75 * SECOND -> "${TimeUnits.MINUTE.plural((diff /MINUTE).toInt())} назад"
        in 45 * MINUTE .. 75 * MINUTE -> "час вперед"
        in -75 * MINUTE .. -45 * MINUTE -> "час назад"
        in 75 * MINUTE .. 22 * HOUR -> "через ${TimeUnits.HOUR.plural((diff /HOUR).toInt())}"
        in -22 * HOUR .. -75 * MINUTE-> "${TimeUnits.HOUR.plural((diff /HOUR).toInt())} назад"
        in 22 * HOUR .. 26 * HOUR -> "день вперед"
        in -26 * HOUR .. -22 * HOUR -> "день назад"
        in 26 * HOUR .. 360 * DAY -> "через ${TimeUnits.DAY.plural((diff / DAY).toInt())}"
        in -360 * DAY .. -26 * HOUR -> "${TimeUnits.DAY.plural((diff / DAY).toInt())} назад"
        in Long.MIN_VALUE .. -360 * DAY -> "более года назад"
        else -> "более чем через год"
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
