package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.DataGenerator

class MainViewModel : ViewModel() {
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()){chats->

        return@map chats.filter{ !it.isArchived }
            .map{ it.toChatItem() }
            .sortedBy { it.id.toInt() }
    }

    fun getChatData() : LiveData<List<ChatItem>> {
        Log.d("M_MainViewModel", "${chats}")
        return chats
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
}