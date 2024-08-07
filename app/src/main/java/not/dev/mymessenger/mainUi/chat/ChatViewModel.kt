package not.dev.mymessenger.mainUi.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import not.dev.mymessenger.core.base.dto.ChatModel
import not.dev.mymessenger.mainUi.main.MainApplication

class ChatViewModel : ViewModel() {
    private val _chatSendMessageEvent = MutableLiveData<ChatSendMessageEvent>()
    val chatSendMessageEvent: LiveData<ChatSendMessageEvent> = _chatSendMessageEvent

    private val _getMessageEvent = MutableLiveData<GetMessageEvent>()
    val getMessageEvent: LiveData<GetMessageEvent> = _getMessageEvent


    private var valueEventListener: ValueEventListener? = null
    private val messageList = mutableListOf<ChatModel>()


    fun getMessage(friendToken: String, myToken: String) {
        _getMessageEvent.value = GetMessageEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val vl = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    snapshot.children.forEach {
                        val chatModel = it.getValue(ChatModel::class.java)
                        chatModel?.let { messageList.add(chatModel) }
                    }
                    _getMessageEvent.value = GetMessageEvent.Success(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    _getMessageEvent.value = GetMessageEvent.Failure(error.message)
                }

            }
            this@ChatViewModel.valueEventListener = vl
            this@ChatViewModel.valueEventListener?.let {
                val token = friendToken + myToken
                val chatId = token.toCharArray().sortedArray().joinToString("")
                MainApplication.dbReference.child("chat").child(chatId).addValueEventListener(it)
            }
        }
    }

    fun sendMessage(message: String, myToken: String, friendToken: String) {
        val chatModel = ChatModel(message, myToken, friendToken)
        viewModelScope.launch(Dispatchers.IO) {
            val token = friendToken + myToken
            val chatId = token.toCharArray().sortedArray().joinToString("")
            MainApplication.dbReference.child("chat").child(chatId).push().setValue(chatModel)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _chatSendMessageEvent.value = ChatSendMessageEvent.Success
                    } else {
                        _chatSendMessageEvent.value =
                            ChatSendMessageEvent.Failure(it.exception?.message.toString())
                    }
                }
                .addOnFailureListener {
                    _chatSendMessageEvent.value =
                        ChatSendMessageEvent.Failure(it.message.toString())
                }
        }

    }

}

sealed class ChatSendMessageEvent() {
    data object Success : ChatSendMessageEvent()
    data class Failure(val message: String) : ChatSendMessageEvent()
    data object Load : ChatSendMessageEvent()
}

sealed class GetMessageEvent {
    data class Success(val message: List<ChatModel>) : GetMessageEvent()
    data class Failure(val message: String) : GetMessageEvent()
    data object Loading : GetMessageEvent()
}