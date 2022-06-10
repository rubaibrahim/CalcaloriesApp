package com.gp2.calcalories.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.databinding.FragmentNotificationsBinding
import com.gp2.calcalories.remote.model.user.entity.UserNotification
import com.gp2.calcalories.remote.model.user.request.GetNotificationRequest
import com.gp2.calcalories.ui.adapters.NotificationAdapter

class NotificationsFragment : BaseFragment() {
    private var _binding: FragmentNotificationsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        repository?.getUserNotification(GetNotificationRequest(
            updated_at = Utility.getCurrentDate("yyyy-MM-dd")
        )){
            if (it.status == HTTPCode.Success) {
                val data = it.user_notifications
                Alert.log("size", data.size.toString())
                if (data.size > 0) {
                    adapter.submitList(data)
                    setViews(viewError = false, viewList = true)
                    return@getUserNotification
                }
            }
            setViews(viewError = true, viewList = false)
        }

    }

    private val adapter = NotificationAdapter(object : GeneralListener {
        override fun onClick(action: Actions, data: Any?) {
            if (action == Actions.VIEW && data is UserNotification) {
                if (data.is_read == 0) {
                    // Update notification status (is_read) to 1
                    repository?.updateUserNotification(data.id){}
                }

                if (data.type == 2) {
                    // View recipes list with data.ids filter
                    findNavController().navigate(NotificationsFragmentDirections
                        .navigateToRecipesSearchFragment(data.ids,false))
                }
            }
        }
    })

    private fun setViews(viewError: Boolean, viewList: Boolean) {
        binding.txtError.visibility = if (viewError) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (viewList) View.VISIBLE else View.GONE
    }
}