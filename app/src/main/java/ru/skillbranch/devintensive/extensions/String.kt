package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16) : String {
    if(count <= 0) return ""
    val trimStr = trimEnd()
    return when(trimStr.length) {
        in 0..count -> trimStr
        else -> trimStr.substring(0,count).trimEnd() + "..."
    }
}

fun String.stripHtml(): String {
    val newString = "<[^<>]+>".toRegex().replace(this, "")
    return "\\s{2,}".toRegex().replace(newString, " ")
}
