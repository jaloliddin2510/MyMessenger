package not.dev.mymessenger.mainUi.auth.signUp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import not.dev.mymessenger.R
import not.dev.mymessenger.core.base.extentions.toast
import not.dev.mymessenger.databinding.FragmentSignUpBinding
import not.dev.mymessenger.mainUi.main.MainActivity
import not.dev.mymessenger.mainUi.main.MainApplication
import not.dev.mymessenger.mainUi.profile.EditProfileActivity

class SignUp : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        action()
        setup()
    }
    private fun action() {
        binding.signUpToIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }
    }

    private fun setup() = with(binding) {
        signUpButton.setOnClickListener {
            val email = signUpTextEmail.text?.toString()
            val password = signUpTextPassword.text?.toString()
            val name = signUpTextName.text?.toString()
            val firstName = signUpTextFirstName.text?.toString()
            if (email.isNullOrEmpty() ||  password.isNullOrEmpty() || name.isNullOrEmpty() || firstName.isNullOrEmpty()) {
                requireContext().toast("You have not filled in a line")
                return@setOnClickListener
            }
            viewModel.signUp(email, password,name,firstName)
        }
        viewModel.signUpEvent.observe(requireActivity()) {
            when (it) {
                is SignUpEvent.Error -> {
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    requireContext().toast(it.message)
                }

                SignUpEvent.Loading -> {
                    signUpProgressBar.visibility = View.VISIBLE
                    signUpButton.visibility = View.GONE
                }
                is SignUpEvent.Success -> {
                    requireContext().toast("Success")
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    it.token?.let { iToken ->
                        MainApplication.sharedPreferences.edit()
                            .putString(MainApplication.TOKEN, iToken).apply()
                        val intent= Intent(requireActivity(), EditProfileActivity::class.java)
                        startActivity(intent)
                    } ?: requireContext().toast("Token is null!")
                }
            }
        }
        viewModel.saveUserEvent.observe(requireActivity()){
            when(it){
                is SaveUserEvent.Error -> {
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    requireContext().toast(it.message)
                }
                SaveUserEvent.Loading -> {
                    signUpProgressBar.visibility = View.VISIBLE
                    signUpButton.visibility = View.GONE

                }
                is SaveUserEvent.Success -> {
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    requireContext().toast("Success")
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            }
        }

    }

}