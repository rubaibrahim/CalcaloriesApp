package com.gp2.calcalories.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gp2.calcalories.common.base.BaseFragment
import com.gp2.calcalories.common.config.setNavigationResult
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.enums.HTTPCode
import com.gp2.calcalories.common.enums.VitaminTypes
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.databinding.FragmentNotificationRecipesBinding
import com.gp2.calcalories.remote.model.meal.entity.MealRecipe
import com.gp2.calcalories.remote.model.meal.request.GetMealRecipeRequest
import com.gp2.calcalories.remote.model.meal.request.PostMealRecipeRequest
import com.gp2.calcalories.remote.model.meal.request.Vitamin
import com.gp2.calcalories.remote.model.meal.response.GetMealRecipesResponse
import com.gp2.calcalories.ui.adapters.MealRecipeAdapter
import com.gp2.calcalories.ui.fragments.home.RecipesFragmentDirections
import org.json.JSONArray

class RecipesSearchFragment : BaseFragment() {
    private var _binding: FragmentNotificationRecipesBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentNotificationRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val args by navArgs<RecipesSearchFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        when {
            args.ids.startsWith("[") -> { //  [1,2,3]
                repository?.getMealRecipes2(PostMealRecipeRequest(
                    vitamins = calculateVitamins(JSONArray(args.ids))
                )){
                    setResult(it)
                }
            }
            args.needResult -> {
                repository?.getMealRecipes(GetMealRecipeRequest()){
                    setResult(it)
                }
            }
            else -> findNavController().popBackStack()
        }
    }

    private fun calculateVitamins(ids: JSONArray): MutableList<Vitamin> {
        val vitamins: MutableList<Vitamin> = mutableListOf()
        Alert.log("IDs:", ids.toString())
        session?.getUser()?.let { user ->
            for (i in 0 until ids.length()) {
                Alert.log("ID: ", ids[i].toString())
                when (ids[i]) {
                    VitaminTypes.PROTEIN -> {
                        var used = 0f
                        user.plans.forEach { plan ->
                            used += plan.totals?.used_vitamin_protein ?: 0f
                        }

                        val vitamin = user.details?.vitamin_protein ?: -1f
                        if (used < vitamin) {
                            vitamins.add(Vitamin(
                                id = VitaminTypes.PROTEIN.toString(),
                                min = ((vitamin - used) / 2).toString(),  //used.toString(),
                                max = (vitamin - used).toString(),
                            ))
                        }
                        Alert.log("PROTEIN vitamin", vitamin.toString())
                        Alert.log("PROTEIN used", used.toString())
                    }
                    VitaminTypes.IRON -> {
                        var used = 0f
                        user.plans.forEach { plan ->
                            used += plan.totals?.used_vitamin_iron ?: 0f
                        }

                        val vitamin = user.details?.vitamin_iron ?: -1f
                        if (used < vitamin) {
                            vitamins.add(Vitamin(
                                id = VitaminTypes.IRON.toString(),
                                min = ((vitamin - used) / 2).toString(),  //used.toString(),
                                max = (vitamin - used).toString(),
                            ))
                        }

                        Alert.log("IRON vitamin", vitamin.toString())
                        Alert.log("IRON used", used.toString())
                    }
                    VitaminTypes.A -> {
                        var used = 0f
                        user.plans.forEach { plan ->
                            used += plan.totals?.used_vitamin_a ?: 0f
                        }

                        val vitamin = user.details?.vitamin_a ?: -1f
                        if (used < vitamin) {
                            vitamins.add(Vitamin(
                                id = VitaminTypes.A.toString(),
                                min = ((vitamin - used) / 2).toString(),  //used.toString(),
                                max = (vitamin - used).toString(),
                            ))
                        }
                        Alert.log("A vitamin", vitamin.toString())
                        Alert.log("A used", used.toString())
                    }
                }
            }
        }
        return vitamins
    }


    private fun setResult(response : GetMealRecipesResponse){
        if (response.status == HTTPCode.Success) {
            val data = response.meal_recipes
            if(data.size > 0) {
                adapter.submitList(data)
                setViews(viewError = false, viewList = true)
                return
            }
        }
        setViews(viewError = true, viewList = false)
    }

    private val adapter = MealRecipeAdapter(object : GeneralListener {
        override fun onClick(action: Actions, data: Any?) {
            if (action == Actions.VIEW && data is MealRecipe) {
                if (args.needResult) {
                    setNavigationResult<MealRecipe>(data, "recipe")
                    findNavController().popBackStack()
                } else {
                    findNavController().navigate(RecipesFragmentDirections
                        .navigateToRecipeDialog(data))
                }
            }
        }
    })

    private fun setViews(viewError: Boolean, viewList: Boolean) {
        binding.txtError.visibility = if (viewError) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (viewList) View.VISIBLE else View.GONE
    }
}