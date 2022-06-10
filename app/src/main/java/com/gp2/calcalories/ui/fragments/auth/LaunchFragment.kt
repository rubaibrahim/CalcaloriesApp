package com.gp2.calcalories.ui.fragments.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.databinding.FragmentAuthLaunchBinding

class LaunchFragment : BaseFragment() {
    private var _binding: FragmentAuthLaunchBinding? = null
    // This property is only valid between onCreateView and  onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentAuthLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val args by navArgs<LaunchFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val isAboutPage = args.isAboutPage
        if (!isAboutPage) {
            Handler(Looper.getMainLooper()).postDelayed({

                // close this page
                findNavController().popBackStack()

                if (session?.getUser() != null) {
                    Alert.log("User is logged in")
                    // go to home page
                    findNavController().navigate(LaunchFragmentDirections.navigateToPlanFragment())

                } else {
                    Alert.log("User is not logged in")
                    // go to login page
                    findNavController().navigate(R.id.loginFragment)

                }
            }, 3 * 1000)
        }
    }

}