package not.dev.mymessenger.mainUi.sing_in

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import not.dev.mymessenger.databinding.ActivitySignInBinding
import not.dev.mymessenger.extentions.toast
import not.dev.mymessenger.mainUi.main.MainApplication
import not.dev.mymessenger.mainUi.main.ui.MainActivity

class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setUp()
        toast("SignIn da")
    }

    fun setUp() = with(binding) {
        signInButton.setOnClickListener {
            toast("error")
            viewModel.signIn(signInTextUsername.text.toString(), signInTextPassword.text.toString())
        }
        viewModel.signInEvent.observe(this@SignIn) {
                when (it) {
                    is SignInEvent.Failure -> {
                        signInProgressBar.visibility = View.GONE
                        signInButton.visibility = View.VISIBLE
                        toast(it.message)
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
                        startActivity(Intent(this@SignIn, MainActivity::class.java))
                        signInProgressBar.visibility = View.GONE
                        signInButton.visibility = View.VISIBLE
                        toast("Success")
                    }
                }
            }
    }
}