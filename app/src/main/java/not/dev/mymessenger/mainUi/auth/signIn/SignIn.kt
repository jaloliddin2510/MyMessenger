package not.dev.mymessenger.mainUi.auth.signIn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import not.dev.mymessenger.R
import not.dev.mymessenger.core.base.extentions.toast
import not.dev.mymessenger.databinding.FragmentSignInBinding
import not.dev.mymessenger.mainUi.main.MainApplication

@Suppress("DEPRECATION")
class SignIn : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
    }

    fun setUp() = with(binding) {
        signInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }
        signInButton.setOnClickListener {
            viewModel.signIn(signInTextUsername.text.toString(), signInTextPassword.text.toString())
        }
        viewModel.signInEvent.observe(requireActivity()) {
            when (it) {
                is SignInEvent.Failure -> {
                    signInProgressBar.visibility = View.GONE
                    signInButton.visibility = View.VISIBLE
                    requireContext().toast(it.message)
                }

                SignInEvent.Loading -> {
                    signInProgressBar.visibility = View.VISIBLE
                    signInButton.visibility = View.GONE
                }

                is SignInEvent.Success -> {
                    it.token?.let { iToken ->
                        MainApplication.sharedPreferences.edit()
                            .putString(MainApplication.TOKEN, iToken).apply()
                    }
                    startActivity(Intent(requireActivity(), not.dev.mymessenger.mainUi.main.MainActivity::class.java))
                    signInProgressBar.visibility = View.GONE
                    signInButton.visibility = View.VISIBLE
                    requireContext().toast("Success")
                }
            }
        }
    }
    val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode== Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handlerResult(task)
        }

    }

    private fun handlerResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account:GoogleSignInAccount?=task.result
            if (account!=null){
                updateUI(account)
            }
            else{
                requireContext().toast("SignIn Failed")
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        MainApplication.fbAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                if (it.isSuccessful) {
                    viewModel._signInEvent.value = SignInEvent.Success("Success", it.result.user?.uid)
                } else {
                    viewModel._signInEvent.value = SignInEvent.Failure(it.exception?.message.toString())
                }
            }
        }.addOnFailureListener {
            viewModel._signInEvent.value = SignInEvent.Failure(it.message.toString())
        }

    }

    fun signInWithGoogle(){
        val signInIntent=googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
}