package not.dev.mymessenger.mainUi.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import not.dev.mymessenger.R
import not.dev.mymessenger.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getBooleanExtra("edit_profile",false)
    }
}