package not.dev.mymessenger.mainUi.homePaging.all

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import not.dev.mymessenger.databinding.FragmentAllBinding
import not.dev.mymessenger.core.base.extentions.toast
import not.dev.mymessenger.mainUi.chat.ChatActivity
import not.dev.mymessenger.mainUi.main.MainApplication
import not.dev.mymessenger.mainUi.main.MainApplication.Companion.sharedPreferences

class AllFragment : Fragment() {
    private lateinit var binding: FragmentAllBinding
    private val vm: AllViewModel by viewModels()
    private val allAdapter by lazy {
        AllAdapter { chatItem ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("chat_user",chatItem)
            startActivity(intent)
        }}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAllBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() = with(binding) {
        recyclerviewAll.adapter = allAdapter
        vm.users.observe(requireActivity()) {
            when (it) {
                is GetChatEvent.Success -> {

                    allAdapter.submitList(it.users.toList())
                }

                is GetChatEvent.Error -> {
                    requireContext().toast(it.message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.removeListener()
    }
}