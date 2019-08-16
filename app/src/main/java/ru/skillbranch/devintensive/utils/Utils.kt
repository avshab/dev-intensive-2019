package ru.skillbranch.devintensive.utils

object Utils {
    //to do fix me
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " ") : String {
        //TODO
        return ""
    }

    fun toInitials(firstName: String, lastName: String) : String {
        //TODO
        return ""
    }
}