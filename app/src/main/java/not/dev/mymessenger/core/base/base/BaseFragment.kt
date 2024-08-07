package not.dev.mymessenger.core.base.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    lateinit var binding:VB

    abstract fun getVB(): VB
    abstract fun setup()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = getVB()
        binding=_binding!!
        setup()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}