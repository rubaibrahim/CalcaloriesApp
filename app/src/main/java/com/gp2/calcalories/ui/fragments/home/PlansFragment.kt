package com.gp2.calcalories.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.databinding.FragmentPlansBinding
import com.gp2.calcalories.remote.model.plan.entity.PlanMeal
import com.gp2.calcalories.remote.model.plan.entity.UserPlan
import com.gp2.calcalories.remote.model.user.request.GetNotificationRequest
import com.gp2.calcalories.remote.model.user.request.PostUserRequest
import com.gp2.calcalories.ui.adapters.PLanAdapter
import java.util.*

class PlansFragment : BaseFragment() {
    private var _binding: FragmentPlansBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val adapter = PLanAdapter(object : GeneralListener {
        override fun onClick(action: Actions, data: Any?) {
            super.onClick(action, data)

            if (data is UserPlan) {
                if (action == Actions.ADD) {
                    findNavController().navigate(PlansFragmentDirections
                        .navigateToPlanAddMealFragment(data.id))

                }
            } else if (data is PlanMeal) {
                if (action == Actions.DELETE) {
                    Alert.alert(requireActivity(), getString(R.string.delete), getString(R.string.are_you_sure), {
                        repository?.deleteUserPlanMeal(data.id){
                            if (it.status == HTTPCode.Success) getUserPlansList()
                        }}, {})
                }
            }
        }
    })

    private fun setViews(viewError: Boolean, viewList: Boolean) {
        binding.txtError.visibility = if (viewError) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (viewList) View.VISIBLE else View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserDetailsData()

        binding.apply {

//            var used = 0
//            session?.getUser()?.plans?.forEach { plan ->
//                used += plan.totals?.used_calories ?: 0
//            }
//            binding.txtUsedCalories.text = used.toString()

            txtCalories.text = String.format(Locale.ENGLISH, " / %d kcal",
                session?.getUser()?.details?.calories)

            txtDate.text = Utility.getCurrentDate("EEEE, dd MMMM, yyyy")

            recyclerView.adapter = adapter
        }


        // To update fcm token
        repository?.updateUser(session?.getUser()?.id ?: 0, PostUserRequest(
            name = session?.getUser()?.name.toString(),
            email = session?.getUser()?.email.toString(),
            fcm_token = session?.getFirebaseToken().toString()
        )) {}

        getUserPlansList()
    }

    private fun getUserPlansList() {
        repository?.getUserPlans(GetNotificationRequest(
            updated_at = Utility.getCurrentDate("yyyy-MM-dd")
        )) {
            if (it.status == HTTPCode.Success) {
                adapter.submitList(it.user_plans)

                val user = session?.getUser()
                user?.plans = it.user_plans
                session?.saveUser(user)

                var usedCalories = 0
                it.user_plans.forEach { plan ->
                    usedCalories += plan.totals?.used_calories ?: 0
                }
                binding.txtUsedCalories.text = usedCalories.toString()

                setViews(viewError = false, viewList = true)
                return@getUserPlans
            }
            setViews(viewError = true, viewList = false)
        }
    }


    private fun checkUserDetailsData() {
        session?.getUser()?.let {
            if (it.details == null || it.details.calories == 0) {
                findNavController().navigate(R.id.navigateToEditDetailsFragment)
            }
        }
    }
}