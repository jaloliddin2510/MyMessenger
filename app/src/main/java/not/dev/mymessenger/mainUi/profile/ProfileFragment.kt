package not.dev.mymessenger.mainUi.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import not.dev.mymessenger.R
import not.dev.mymessenger.databinding.FragmentProfileBinding
import not.dev.mymessenger.mainUi.main.MainActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        auth=FirebaseAuth.getInstance()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        binding.back.setOnClickListener {
            val intent= Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }
        if (auth.currentUser!=null){
            binding.profileName.text=auth.currentUser?.displayName?:"Error Name"
            binding.profileEmail.text=auth.currentUser?.email?:"Error Email"
            binding.profileToken.text=auth.currentUser?.uid?:"Error Token"
        }
    }
}