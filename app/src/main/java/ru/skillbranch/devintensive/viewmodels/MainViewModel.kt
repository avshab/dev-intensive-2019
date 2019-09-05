package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.*
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.DataGenerator

class MainViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()){chats->

        val item = chats.filter{ it.isArchived }.sortedBy { it.lastMessageDate()?.time }.lastOrNull()?.toChatItem()
        val list =  chats.filter{ !it.isArchived }
            .map{ it.toChatItem() }
            .sortedBy { it.id.toInt() }

        var returnList: MutableList<ChatItem> = mutableListOf()
        if(item != null) {
            returnList.add(item)
        }
        returnList.addAll( list )

        return@map returnList// as List<ChatItem>
    }

    fun getChatData() : LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chats.value!!


            result.value = if (queryStr.isEmpty()) chats
            else chats.filter { it.title.contains(queryStr, true) }
        }
        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

//    private fun loadChats(): List<ChatItem> {
//        val chats = chatRepository.loadChats()
//
//        return chats.map{ it.toChatItem() }
//            .sortedBy { it.id.toInt() }
//    }
//
//    fun addItems() {
//        val newItems = DataGenerator.generateChatsWithOffset(chats.value!!.size, 5 ).map { it.toChatItem() }
//        val copy = chats.value!!.toMutableList()
//        copy.addAll(newItems)
//        chats.value = copy.sortedBy { it.id.toInt() }
//    }


    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArhive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}