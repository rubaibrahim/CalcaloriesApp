package com.gp2.calcalories.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.ProfileSettings
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.common.model.ProfileSetting
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.databinding.FragmentHomeProfileBinding
import com.gp2.calcalories.ui.adapters.ProfileSettingAdapter


class ProfileFragment : BaseFragment() {

    private var _binding: FragmentHomeProfileBinding? = null
    // This property is only valid between onCreateView and  onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentHomeProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        Alert.log("FCM UserPreferences", session?.getFirebaseToken().toString())

        val itemList: MutableList<ProfileSetting> = ArrayList()

        if (session?.getUser() != null) {
            itemList.add(ProfileSetting(ProfileSettings.EDIT_ACCOUNT, getString(R.string.edit_account)))
            itemList.add(ProfileSetting(ProfileSettings.PERSONAL_DETAILS, getString(R.string.personal_details)))
        } else {
            itemList.add(ProfileSetting(ProfileSettings.LOGIN, getString(R.string.sign_in)))
        }
        binding.recyclerView.adapter = ProfileSettingAdapter(itemList, listener)
    }

    val listener: GeneralListener = object : GeneralListener{
        override fun onClick(action: ProfileSettings) {
            super.onClick(action)
            when (action) {
                ProfileSettings.EDIT_ACCOUNT -> {
                    if (session?.getUser() != null)
                        findNavController().navigate(R.id.navigateToEditAccountFragment)
                    else
                        Utility.startAppAgain(requireContext())
                }
                ProfileSettings.PERSONAL_DETAILS -> {
                    if (session?.getUser() != null)
                        findNavController().navigate(R.id.navigateToEditDetailsFragment)
                    else
                        Utility.startAppAgain(requireContext())
                }
                ProfileSettings.LOGIN -> {
                    Utility.startAppAgain(requireContext())
                }
            }
        }
    }
}