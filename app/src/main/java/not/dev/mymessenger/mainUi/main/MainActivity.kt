package not.dev.mymessenger.mainUi.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import not.dev.mymessenger.R
import not.dev.mymessenger.databinding.ActivityMainBinding
import not.dev.mymessenger.mainUi.auth.AuthenticationActivity
import not.dev.mymessenger.mainUi.homePaging.ViewPagerAdapter
import not.dev.mymessenger.mainUi.profile.EditProfileActivity

class MainActivity : AppCompatActivity() {

    private val token by lazy {
        MainApplication.sharedPreferences.getString(MainApplication.TOKEN, "")
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Pager()
        isLogin()
        binding.profile.setOnClickListener {
            val intent=Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun Pager() {
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(this)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Friends"
                2 -> "Group"
                else -> null
            }
        }.attach()
    }

    fun isLogin() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser==null){
            val intent = Intent(this, AuthenticationActivity::class.java)
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        else{
            if (token.isNullOrEmpty()){
                val intent=Intent(this,AuthenticationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                findNavController(R.id.fragmentContainerView2).navigate(R.id.action_splash_to_signIn)
                startActivity(intent)
            }
        }
    }
}