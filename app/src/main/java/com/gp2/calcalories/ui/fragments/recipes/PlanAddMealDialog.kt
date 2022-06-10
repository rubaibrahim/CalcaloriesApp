package com.gp2.calcalories.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gp2.calcalories.R
import com.gp2.calcalories.common.base.BaseBottomDialog
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.common.util.Utility
import com.gp2.calcalories.databinding.DialogPlanAddMealBinding
import com.gp2.calcalories.remote.model.plan.request.PostUserPlanMealRequest


class PlanAddMealDialog : BaseBottomDialog() {
    private var _binding: DialogPlanAddMealBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = DialogPlanAddMealBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val args by navArgs<PlanAddMealDialogArgs>()
    var request: PostUserPlanMealRequest = PostUserPlanMealRequest()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.recipe != null) {
            // 1- Add recipe to request
            args.recipe?.let {
                request = PostUserPlanMealRequest(
                    user_plan_id = 0,
                    name = it.name,
                    calories = it.calories,
                    vitamin_protein = it.vitamin_protein,
                    vitamin_iron = it.vitamin_iron,
                    vitamin_a = it.vitamin_a,
                    updated_at = Utility.getCurrentDate("yyyy-MM-dd")
                )
            }
            // 2- View all user plans
            viewPlans()

            binding.btnSave.setOnClickListener {
                repository?.postUserPlanMeal(request){
                    if(it.status == HTTPCode.Success){
                        Alert.toast(requireContext(), R.string.saved_successfully)
                        // close page
                        findNavController().popBackStack()
                    }
                }
            }
        } else {
            dismiss()
        }
    }

    private fun viewPlans() {
        session?.getUser()?.plans?.let { plans ->
            binding.spinnerPlans.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                plans.map { it.meal_type?.name })

            binding.spinnerPlans.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long,
                    ) {
                        request.user_plan_id = plans[position].id
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                        binding.spinnerPlans.setSelection(0)
                    }
                }
        }
    }
}


