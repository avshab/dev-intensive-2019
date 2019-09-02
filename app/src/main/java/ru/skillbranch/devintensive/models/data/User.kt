package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*


data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = Date(),
    val isOnline: Boolean = false
) {

    constructor(id: String, firstName: String?, lastName: String?) : this(
        id  = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, "John", "Doe")

    init {
        println("It's  Alive!!!\n" +
                "${if(lastName==="Doe") "His name id $firstName $lastName" else "And his name is $firstName $lastName"}\n" +
                "${getIntro()}")
    }

    private fun getIntro() = """
        $firstName $lastName
    """.trimIndent()

    fun printMe() = println("""
             id: $id
             firstName: $firstName
             lastName: $lastName
             avatar: $avatar
             rating: $rating
             respect: $respect
             lastVisit: $lastVisit
             isOnline: $isOnline
        """.trimIndent())

    fun toUserItem() : UserItem {
        val lastActivite = when{
            lastVisit == null -> "Еще ни разу не был"
            isOnline -> "online"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
        }

        return UserItem(
            id,
            "${firstName.orEmpty()} ${lastName.orEmpty()}",
            Utils.toInitials(firstName, lastName),
            avatar,
            lastActivite,
            false,
            isOnline
        )
    }

    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?) : User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(
                id = "$lastId",
                firstName = firstName,
                lastName = lastName
            )
        }
    }

    class Builder {
        private var id: String = ""
        private var firstName: String? = "John"
        private var lastName: String? = "Doe"
        private var avatar: String? = null
        private var rating: Int = 0
        private var respect: Int = 0
        private var lastVisit: Date? = null
        private var isOnline: Boolean = false

        fun id(value: String) = apply { id = value }

        fun firstName(value: String?) = apply { firstName = value }

        fun lastName(value: String?) = apply { lastName = value }

        fun avatar(value: String?) = apply { avatar = value }

        fun rating(value: Int) = apply { rating = value }

        fun respect(value: Int) = apply { respect = value }

        fun lastVisit(value: Date?) = apply { lastVisit = value }

        fun isOnline(value: Boolean) = apply { isOnline = value }

        fun build() : User? {
            if (id.isEmpty()) return null
            return User(
                id,
                firstName,
                lastName,
                avatar,
                rating,
                respect,
                lastVisit,
                isOnline
            )
        }
    }
}