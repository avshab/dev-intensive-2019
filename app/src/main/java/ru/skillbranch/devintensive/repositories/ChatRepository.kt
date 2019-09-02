package ru.skillbranch.devintensive.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.Chat

object ChatRepository {
    private val chats = CacheManager.loadChats()

    fun loadChats() : MutableLiveData<List<Chat>> {
        //add object DataGenerator in utils (time: 1: 04, 1:07 tutorial 5)
        return chats
    }

    fun update(chat: Chat) {
        val copy = chats.value!!.toMutableList()
        val ind = chats.value!!.indexOfFirst{ it.id == chat.id }
        if(ind == -1) return
        copy[ind] = chat
        chats.value = copy

    }

    fun find(chatId: String): Chat? {
        val ind = chats.value!!.indexOfFirst { it.id == chatId }
        return chats.value!!.getOrNull(ind)
    }

    fun getArchiveChatsCount(): Int {
        return chats.value!!.filter { it.isArchived }.size
    }

    fun getShortDescription(): String? {
        return chats.value!!.filter { it.isArchived }.lastOrNull()?.lastMessageShort()?.first ?: "message test"
    }
    fun getLastDate(): String? {
        return chats.value!!.filter { it.isArchived }.lastOrNull()?.lastMessageDate()?.shortFormat() ?: "date test"
    }

    fun getLastAuthor(): String? {
        return chats.value!!.filter { it.isArchived }.lastOrNull()?.lastMessageShort()?.second ?: "author test"
    }

    fun getArchiveUnreadMessageCount(): Int {
        return chats.value!!.filter { it.isArchived }.sumBy { it.unreadableMessageCount() }
    }
}