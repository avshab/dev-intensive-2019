package ru.skillbranch.devintensive.utils

object Utils {
    //to do fix me
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        var parts: List<String>? = fullName?.split(" ")

        parts = parts?.filter { !it.isEmpty() }

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " ") : String {
        val list = payload.split(" ")
            .filter { !it.isEmpty() }

        var arrayList = ArrayList<String>()
        for(str in list) {
            val currentString = str.toCharArray().map { translateMap[it.toString()] ?: "" }.joinToString("")
            arrayList.add(currentString)
        }
        return arrayList.joinToString(divider)

    }

    fun toInitials(firstName: String?, lastName: String?) : String? {
        val nameLet = firstName?.trim()?.getOrNull(0)
        val familyLet = lastName?.trim()?.getOrNull(0)
        val initials =  "${nameLet ?: ""}${familyLet ?: ""}"

        if (initials.isEmpty()) return null
        return initials.toUpperCase()
    }

    val translateMap = mapOf(
        "а" to "a",

        "б" to "b",

        "в" to "v",

        "г" to "g",

        "д" to "d",

        "е" to "e",

        "ё" to "e",

        "ж" to "zh",

        "з" to "z",

        "и" to "i",

        "й" to "i",

        "к" to "k",

        "л" to "l",

        "м" to "m",

        "н" to "n",

        "о" to "o",

        "п" to "p",

        "р" to "r",

        "с" to "s",

        "т" to "t",

        "у" to "u",

        "ф" to "f",

        "х" to "h",

        "ц" to "c",

        "ч" to "ch",

        "ш" to "sh",

        "щ" to "sh'",

        "ъ" to "",

        "ы" to "i",

        "ь" to "",

        "э" to "e",

        "ю" to "yu",

        "я" to "ya"
    )

}