package not.dev.mymessenger.mainUi.homePaging

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import not.dev.mymessenger.mainUi.homePaging.all.AllFragment
import not.dev.mymessenger.mainUi.homePaging.friends.FriendsFragment
import not.dev.mymessenger.mainUi.homePaging.main_group.MainGroupFragment

class ViewPagerAdapter(fragmentActivity:FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int =3
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AllFragment()
            1 -> FriendsFragment()
            2 -> MainGroupFragment()
            else -> AllFragment()
        }
    }
}