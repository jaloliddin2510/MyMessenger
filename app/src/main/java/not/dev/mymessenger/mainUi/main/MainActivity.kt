package not.dev.mymessenger.mainUi.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import not.dev.mymessenger.R
import not.dev.mymessenger.databinding.ActivityMainBinding
import not.dev.mymessenger.mainUi.homePaging.ViewPagerAdapter
import not.dev.mymessenger.mainUi.profile.EditProfileActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Pager()
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

    fun profile(){
        binding.profile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))

        }
    }
}