package com.gp2.calcalories.ui.fragments.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.databinding.FragmentProgressBinding
import com.gp2.calcalories.remote.model.user.entity.User
import com.gp2.calcalories.remote.model.user.request.GetNotificationRequest
import java.util.*

class ProgressFragment : BaseFragment() {
    private var _binding: FragmentProgressBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session?.getUser()?.let { user ->
            binding.txtUser.text = user.name
            binding.txtDate.text = Utility.getCurrentDate("EEEE, dd MMMM, yyyy")

            setCalories(user)
            setProtein(user)
            setIron(user)
            setA(user)

            updateUserPlans()
        }
    }

    private fun updateUserPlans(){
        repository?.getUserPlans(GetNotificationRequest(
            updated_at = Utility.getCurrentDate("yyyy-MM-dd")
        )){
            if (it.status == HTTPCode.Success) {
                session?.getUser()?.let { user->
                    user.plans = it.user_plans
                    session?.saveUser(user)

                    setCalories(user)
                    setProtein(user)
                    setIron(user)
                    setA(user)
                }
            }
        }
    }


    private fun setCalories(it:User){
        var used = 0
        var remaining = 0

        it.plans.forEach { plan ->
            used += plan.totals?.used_calories ?: 0
            remaining += plan.totals?.remaining_calories ?: 0
        }

        binding.txtCalories.text = (it.details?.calories ?: 0).toString()
        binding.txtUsed.text = used.toString()
        binding.txtRemaining.text = remaining.toString()

            binding.progressBar1.max = it.details?.calories ?: 0
            binding.progressBar1.progress = used

            val calories = it.details?.calories ?: 0
            binding.progressBar1.progressTintList = getCaloriesColor(used, calories)
    }

    private fun setProtein(it:User){
        var used = 0f
        it.plans.forEach { plan ->
            used += plan.totals?.used_vitamin_protein ?: 0f
        }

        binding.txtProtein.text = String.format(Locale.ENGLISH,"%s g",it.details?.vitamin_protein.toString())
        binding.txtUsedProtein.text = used.toString()

        if(used > 0) {
            binding.progressBarProtein.max = it.details?.vitamin_protein!!.toInt()
            binding.progressBarProtein.progress = used.toInt()
            binding.progressBarProtein.progressTintList =
                getVitaminColor(used, it.details.vitamin_protein)
        }
    }
    private fun setIron(it:User){
        var used = 0f
        it.plans.forEach { plan ->
            used += plan.totals?.used_vitamin_iron ?: 0f
        }

        binding.txtIron.text = String.format(Locale.ENGLISH,"%s mg",it.details?.vitamin_iron.toString())
        binding.txtUsedIron.text = used.toString()

        if(used > 0) {
            binding.progressBarIron.max = it.details?.vitamin_iron!!.toInt()
            binding.progressBarIron.progress = used.toInt()
            binding.progressBarIron.progressTintList =
                getVitaminColor(used, it.details.vitamin_iron)
        }
    }
    private fun setA(it:User){
        var used = 0f
        it.plans.forEach { plan ->
            used += plan.totals?.used_vitamin_a ?: 0f
        }

        binding.txtVitaminA.text = String.format(Locale.ENGLISH,"%s mcg",it.details?.vitamin_a.toString())
        binding.txtUsedA.text = used.toString()

        if(used > 0) {
            binding.progressBarA.max = it.details?.vitamin_a!!.toInt()
            binding.progressBarA.progress = used.toInt()
            binding.progressBarA.progressTintList = getVitaminColor(used, it.details.vitamin_a)
        }
    }



    // Utils =======================================================================================

    private fun getCaloriesColor(used:Int, total:Int) : ColorStateList{
        if(used >= 0 && used < (total / 100 * 60)){
            return ColorStateList.valueOf(resources.getColor(R.color.green))
        }else if(used >= (total / 100 * 60) && used < total){
            return ColorStateList.valueOf(resources.getColor(R.color.yallow))
        }else {
            return ColorStateList.valueOf(resources.getColor(R.color.red2))
        }
    }

    private fun getVitaminColor(used:Float, total:Float) : ColorStateList{
        if(used >= 0 && used < (total / 100 * 30)){
            return ColorStateList.valueOf(resources.getColor(R.color.red2))
        }else if(used >= (total / 100 * 30) && used < total){
            return ColorStateList.valueOf(resources.getColor(R.color.yallow))
        }else {
            return ColorStateList.valueOf(resources.getColor(R.color.green))
        }
    }

}