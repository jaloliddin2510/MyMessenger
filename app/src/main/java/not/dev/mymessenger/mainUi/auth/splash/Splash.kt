package not.dev.mymessenger.mainUi.auth.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import not.dev.mymessenger.R
import not.dev.mymessenger.databinding.FragmentSplashBinding
import not.dev.mymessenger.mainUi.homePaging.ViewPagerAdapter

class Splash : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.splashVp
        val items = listOf(
            SplashItem(R.drawable.uno, "Welcome to Uno Messenger"),
            SplashItem(
                R.drawable.firebase,
                "This messenger was created together with the FireBase platform"
            ),
            SplashItem(
                R.drawable.icon_gmail,
                "To verify your identity, it is done by sending a code to your gmail account or by verifying directly on the device"
            )
        )
        val adapter = SplashAdapter(items)
        viewPager.adapter = adapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        runnable = Runnable {
            if (currentPage < adapter.itemCount - 1) {
                currentPage++
                viewPager.setCurrentItem(currentPage, true)
                binding.splashBtn.text =
                    if (currentPage == adapter.itemCount - 1) "Get Started" else "Next"
                handler.postDelayed(runnable, 5000)
            } else {
                binding.splashBtn.text = "Get Started"
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                binding.splashBtn.text =
                    if (currentPage == adapter.itemCount - 1) "Get Started" else "Next"
                handler.removeCallbacks(runnable)
                if (currentPage < adapter.itemCount - 1) {
                    handler.postDelayed(runnable, 5000)
                }
            }
        })

        binding.splashBtn.setOnClickListener {
            if (currentPage == adapter.itemCount - 1) {
                findNavController().navigate(R.id.action_splash_to_signUp)
            } else {
                currentPage++
                viewPager.setCurrentItem(currentPage, true)
                binding.splashBtn.text =
                    if (currentPage == adapter.itemCount - 1) "Get Started" else "Next"
                handler.removeCallbacks(runnable)
                if (currentPage < adapter.itemCount - 1) {
                    handler.postDelayed(runnable, 5000)
                }
            }
        }

        handler.postDelayed(runnable, 5000)


    }


}