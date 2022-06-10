package com.gp2.calcalories.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.databinding.FragmentRecipesBinding
import com.gp2.calcalories.remote.model.general.request.GeneralRequest
import com.gp2.calcalories.remote.model.meal.entity.MealRecipe
import com.gp2.calcalories.remote.model.meal.entity.MealType
import com.gp2.calcalories.remote.model.meal.request.GetMealRecipeRequest
import com.gp2.calcalories.ui.adapters.MealRecipeAdapter

class RecipesFragment : BaseFragment() {
    private var _binding: FragmentRecipesBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val adapter = MealRecipeAdapter(object : GeneralListener {
        override fun onClick(action: Actions, data: Any?) {
            if (action == Actions.VIEW && data is MealRecipe) {
                findNavController().navigate(
                    RecipesFragmentDirections.navigateToRecipeDialog(data)
                )
            }
        }
    })

    private fun setViews(viewError: Boolean, viewList: Boolean) {
        binding.txtError.visibility = if (viewError) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (viewList) View.VISIBLE else View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        getMealTypesList()
    }

    private fun getMealTypesList() {
        repository?.getMealTypes(GeneralRequest()) {
            if (it.status == HTTPCode.Success) {
                it.male_types.let { mealTypes ->

                    if (mealTypes.first().id != 0)
                        mealTypes.add(0, MealType(0, "All"))

                    binding.spinnerMealTypes.adapter = ArrayAdapter(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        mealTypes.map { it.name })

                    binding.spinnerMealTypes.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(
                                parentView: AdapterView<*>?,
                                selectedItemView: View?,
                                position: Int,
                                id: Long,
                            ) {
                                try {
                                    val mealTypeId = mealTypes[position].id
                                    getMealRecipesList(if (mealTypeId > 0) mealTypeId.toString() else "")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onNothingSelected(parentView: AdapterView<*>?) {
                                binding.spinnerMealTypes.setSelection(0)
                            }
                        }
                }
                return@getMealTypes
            }
            setViews(viewError = true, viewList = false)
        }
    }

    private fun getMealRecipesList(meal_type_id: String = "") {

        repository?.getMealRecipes(GetMealRecipeRequest(meal_type_id)) {
            if (it.status == HTTPCode.Success) {
                val data = it.meal_recipes
                if (data.size > 0) {
                    adapter.submitList(data)
                    setViews(viewError = false, viewList = true)
                    return@getMealRecipes
                }
            }
            setViews(viewError = true, viewList = false)
        }
    }

}