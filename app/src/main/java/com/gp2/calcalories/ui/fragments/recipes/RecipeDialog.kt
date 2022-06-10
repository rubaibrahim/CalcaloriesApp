package com.gp2.calcalories.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gp2.calcalories.common.base.BaseBottomDialog
import com.gp2.calcalories.common.config.fullScreen
import com.gp2.calcalories.databinding.FragmentRecipeBinding
import com.gp2.calcalories.ui.activities.MainActivity
import com.gp2.calcalories.ui.fragments.home.PlansFragmentDirections
import java.util.*

class RecipeDialog : BaseBottomDialog() {
    private var _binding: FragmentRecipeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onStart() {
        super.onStart()
        fullScreen()
    }


    private val args by navArgs<RecipeDialogArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.mealRecipe

        (requireActivity() as MainActivity).title = data.name

        binding.apply {
            txtName.text = data.name
            txtMealType.text = data.meal_type?.name
            txtDetails.text = data.details
            txtTotals.text = String.format(Locale.ENGLISH,
                "Calories: %s, Protein: %s, Iron: %s, Vitamin A: %s",
                data.calories.toString(),
                data.vitamin_protein.toString(),
                data.vitamin_iron.toString(),
                data.vitamin_a.toString())

            image.setImageURI(data.img_url)

            btnAdd.setOnClickListener {
                findNavController().navigate(PlansFragmentDirections
                    .navigateToPlanAddMealDialog(args.mealRecipe))
            }
            btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}