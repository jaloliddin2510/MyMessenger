package not.dev.mymessenger.mainUi.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import not.dev.mymessenger.R
import not.dev.mymessenger.databinding.FragmentEditProfileBinding
import not.dev.mymessenger.databinding.FragmentProfileBinding

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

    }

    private fun setup() = with(binding) {
        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        editSave.setOnClickListener {

        }
    }

}
