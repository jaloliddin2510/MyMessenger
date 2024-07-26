package not.dev.mymessenger.mainUi.homePaging.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import not.dev.mymessenger.databinding.FragmentAllBinding
import not.dev.mymessenger.extentions.toast

class AllFragment : Fragment() {
    private lateinit var binding: FragmentAllBinding
    private val vm: AllViewModel by viewModels()
    private val allAdapter by lazy { AllAdapter() }
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
        recyclerviewAll.adapter=allAdapter
        vm.users.observe(viewLifecycleOwner) { event ->
            when (event) {
                is GetChatEvent.Success -> {
                    allAdapter.submitList(event.users)
                }

                is GetChatEvent.Error -> {
                    requireContext().toast(event.message)
                }
            }
        }
    }
}