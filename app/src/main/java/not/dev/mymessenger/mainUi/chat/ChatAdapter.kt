package not.dev.mymessenger.mainUi.chat

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import not.dev.mymessenger.core.base.dto.ChatModel
import not.dev.mymessenger.core.base.extentions.gone
import not.dev.mymessenger.core.base.extentions.visible
import not.dev.mymessenger.databinding.ItemMessageBinding
import not.dev.mymessenger.mainUi.main.MainApplication

class ChatAdapter :
    androidx.recyclerview.widget.ListAdapter<ChatModel, ChatAdapter.ChatVH>(diffUtil) {

    inner class ChatVH(
        private val binding: ItemMessageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatModel: ChatModel) = with(binding) {
            tvMessage.text = chatModel.message
            val myToken = MainApplication.sharedPreferences.getString(MainApplication.TOKEN, "")
            if (myToken != chatModel.myToken) {
                val LP = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                LP.gravity = Gravity.START
                LP.marginStart = 300
                binding.RVChat.layoutParams = LP
            } else {
                val LP = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                LP.gravity = Gravity.END
                LP.marginEnd = 300
                binding.RVChat.layoutParams = LP
            }
        }

        fun failure() {
            binding.failedMessage.visible()
            binding.sendProgressBar.gone()
            binding.sendTimeMessage.gone()
            binding.readMessage.gone()
        }

        fun loading() {
            binding.failedMessage.gone()
            binding.sendProgressBar.visible()
            binding.sendTimeMessage.gone()
            binding.readMessage.gone()
        }

        fun success() {
            binding.failedMessage.gone()
            binding.sendProgressBar.gone()
            binding.sendTimeMessage.visible()
            binding.readMessage.visible()
        }

    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ChatModel>() {
            override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        return ChatVH(
            ItemMessageBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        holder.bind(getItem(position))
    }
}