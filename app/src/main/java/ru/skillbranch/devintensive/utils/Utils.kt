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
            val currentString = str.toCharArray().map { translateMap[it.toString()] ?: it }.joinToString("")
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
        "а" to "a", "A" to "A",
        "б" to "b", "Б" to "B",
        "в" to "v", "В" to "V",
        "г" to "g", "Г" to "G",
        "д" to "d", "Д" to "D",
        "е" to "e", "Е" to "E",
        "ё" to "e", "Ё" to "E",
        "ж" to "zh", "Ж" to "Zh",
        "з" to "z", "З" to "Z",
        "и" to "i", "И" to "I",
        "й" to "i", "Й" to "I",
        "к" to "k", "К" to "K",
        "л" to "l", "Л" to "L",
        "м" to "m", "М" to "M",
        "н" to "n", "Н" to "N",
        "о" to "o", "О" to "O",
        "п" to "p", "П" to "P",
        "р" to "r", "Р" to "R",
        "с" to "s", "С" to "S",
        "т" to "t", "Т" to "T",
        "у" to "u", "У" to "U",
        "ф" to "f", "Ф" to "F",
        "х" to "h", "Х" to "H",
        "ц" to "c", "Ц" to "C",
        "ч" to "ch", "Ч" to "Ch",
        "ш" to "sh", "Ш" to "Sh",
        "щ" to "sh'", "Щ" to "Sh'",
        "ъ" to "", "ъ" to "",
        "ы" to "i", "Ы" to "I",
        "ь" to "", "ь" to "",
        "э" to "e", "Э" to "E",
        "ю" to "yu", "Ю" to "Yu",
        "я" to "ya", "Я" to "Ya"
    )

    fun checkGit(path:String) : Boolean{
        val ignore:List<String> = listOf("enterprise", "features", "topics" , "collections", "trending," +
                "events", "marketplace", "pricing", "nonprofit", "customer-stories", "security", "login", "join")

        var strCatName = path
        if (strCatName.isEmpty())
            return true
        if (!strCatName.contains("github.com"))
            return false
        if (strCatName.contains("https://") && strCatName.substringBefore("https://").isNotEmpty())
            return false
        strCatName = strCatName.substringAfter("https://")
        if (strCatName.contains("www.") && strCatName.substringBefore("www.").isNotEmpty())
            return false
        strCatName = strCatName.substringAfter("www.")
        if (strCatName.substringBefore("github.com/").isNotEmpty())
            return false
        strCatName = strCatName.substringAfter("github.com/")
        if (strCatName.isNotEmpty() && !strCatName.contains("/") && !ignore.contains(strCatName)) {
            return true
        }
        return false
    }
}