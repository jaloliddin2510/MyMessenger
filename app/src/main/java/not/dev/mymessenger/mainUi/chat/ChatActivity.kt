package not.dev.mymessenger.mainUi.chat

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import not.dev.mymessenger.core.base.dto.UserModel
import not.dev.mymessenger.databinding.ActivityChatBinding
import not.dev.mymessenger.databinding.ItemMessageBinding
import not.dev.mymessenger.mainUi.main.MainApplication

@Suppress("DEPRECATION")
class ChatActivity : AppCompatActivity() {
    private val myToken by lazy {
        MainApplication.sharedPreferences.getString(MainApplication.TOKEN, "")
    }
    private val vm: ChatViewModel by viewModels()
    private val adapterChat by lazy { ChatAdapter() }
    private var friendToken: String? = null

    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup(): Unit = with(binding) {
        chatRV.adapter=adapterChat
        val userModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra("chat_user", UserModel::class.java)
        } else {
            intent?.getSerializableExtra("chat_user") as? UserModel
        }
        userModel?.let {
            friendToken = it.userToken
            friendName.text = it.name
        }
        chatMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateVisibility()
            }
        })
        vm.getMessage(friendToken!!,myToken!!)
        chatSendMessage.setOnClickListener {
            sendMessage()
        }
        observers()
        chatBack.setOnClickListener {
            finish()
        }
    }

    private fun observers() {
        with(vm){
            getMessageEvent.observe(this@ChatActivity){
                when(it){
                    is GetMessageEvent.Failure -> {
                        adapterChat.ChatVH(ItemMessageBinding.inflate(layoutInflater)).failure()
                    }
                    GetMessageEvent.Loading -> {
                        adapterChat.ChatVH(ItemMessageBinding.inflate(layoutInflater)).loading()
                    }
                    is GetMessageEvent.Success -> {
                        adapterChat.ChatVH(ItemMessageBinding.inflate(layoutInflater)).success()
                        adapterChat.submitList(it.message.toList())
                    }
                }
            }
        }
    }

    private fun updateVisibility() = with(binding) {
        if (chatMessage.text.isNullOrEmpty()) {
            chatSendMessage.visibility = View.GONE
            chatMedia.visibility = View.VISIBLE
            chatVoiceMessage.visibility = View.VISIBLE
        } else {
            chatSendMessage.visibility = View.VISIBLE
            chatMedia.visibility = View.GONE
            chatVoiceMessage.visibility = View.GONE
        }
    }

    fun sendMessage() = with(binding) {
        if (!chatMessage.text.isNullOrEmpty()) {
            vm.sendMessage(chatMessage.text.toString(), friendToken!!, myToken!!)
            chatMessage.text=null
        }
    }
}