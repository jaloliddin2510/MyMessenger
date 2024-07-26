package not.dev.mymessenger.mainUi.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import not.dev.mymessenger.databinding.ActivitySignUpBinding
import not.dev.mymessenger.extentions.toast
import not.dev.mymessenger.mainUi.main.MainActivity
import not.dev.mymessenger.mainUi.main.MainApplication
import not.dev.mymessenger.mainUi.sing_in.SignIn

@Suppress("UNREACHABLE_CODE")
class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        action()
        setup()
    }

    fun action() {
        binding.signUpToIn.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }
    }

    private fun setup() = with(binding) {
        signUpButton.setOnClickListener {
            val email = signUpTextEmail.text?.toString()
            val username = signUpTextUsername.text?.toString()
            val password = signUpTextPassword.text?.toString()
            if (email.isNullOrEmpty() || username.isNullOrEmpty() || password.isNullOrEmpty()) {
                toast("You have not filled in a line")
                return@setOnClickListener
            }
            viewModel.signUp(email, username, password)
        }
        viewModel.signUpEvent.observe(this@SignUp) {
            when (it) {
                is SignUpEvent.Error -> {
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    toast(it.message)
                }

                SignUpEvent.Loading -> {
                    signUpProgressBar.visibility = View.VISIBLE
                    signUpButton.visibility = View.GONE
                }

                is SignUpEvent.Success -> {
                    toast("Success")
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    it.token?.let { iToken ->
                        MainApplication.sharedPreferences.edit()
                            .putString(MainApplication.TOKEN, iToken).apply()
                        viewModel.saveUserDB(signUpTextUsername.text.toString(),iToken)
                        startActivity(Intent(this@SignUp, MainActivity::class.java))
                    } ?: toast("Token is null!")
                }
            }
        }
        viewModel.saveUserEvent.observe(this@SignUp){
            when(it){
                is SaveUserEvent.Error -> {
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    toast(it.message)
                }
                SaveUserEvent.Loading -> {
                    signUpProgressBar.visibility = View.VISIBLE
                    signUpButton.visibility = View.GONE

                }
                is SaveUserEvent.Success -> {
                    signUpProgressBar.visibility = View.GONE
                    signUpButton.visibility = View.VISIBLE
                    toast("Success")
                    startActivity(Intent(this@SignUp, MainActivity::class.java))
                }
            }
        }

    }
}