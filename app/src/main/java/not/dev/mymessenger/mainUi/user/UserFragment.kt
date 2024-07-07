package not.dev.mymessenger.mainUi.user

import not.dev.mymessenger.base.BaseFragment
import not.dev.mymessenger.databinding.FragmentUserBinding

class UserFragment :BaseFragment<FragmentUserBinding>(){
    override fun getVB(): FragmentUserBinding {
        return FragmentUserBinding.inflate(layoutInflater)
    }

    override fun setup() {

    }
}