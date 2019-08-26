package ru.skillbranch.devintensive.utils

import ru.skillbranch.devintensive.models.Chat
import ru.skillbranch.devintensive.models.data.ChatItem

object DataGenerator  {
    fun generateChats(i: Int): List<Chat> {
        val c1 = Chat("12", "lesdjc", listOf(), mutableListOf(), false)
        val c2 = Chat("12", "lesdjc", listOf(), mutableListOf(), false)
        return listOf(c1, c2)
    }

    fun generateChatsWithOffset(size: Any, i: Int): List<Chat> {
        val c1 = Chat("12", "lesdjc", listOf(), mutableListOf(), false)
        val c2 = Chat("12", "lesdjc", listOf(), mutableListOf(), false)
        return listOf(c1, c2)
    }

    private val maleNames = listOf(
        "Robin",
        "James",
        "John"
    )
}