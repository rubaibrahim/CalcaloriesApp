package com.gp2.calcalories.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.common.util.Alert
import com.gp2.calcalories.databinding.ItemMealRecipeBinding
import com.gp2.calcalories.remote.model.meal.entity.MealRecipe
import java.util.*

class MealRecipeAdapter(private val listener: GeneralListener) :
    ListAdapter<MealRecipe, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemMealRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        (holder as ViewHolder).bind(data)
    }

    inner class ViewHolder(val binding: ItemMealRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MealRecipe) {

            binding.apply {
                Alert.log(data.id.toString() + ": " +data.img_url)
                image.setImageURI(data.img_url)

                txtName.text = data.name
                txtMealType.text = data.meal_type?.name

                txtTotals.text = String.format(Locale.ENGLISH,
                        "Calories: %s, Protein: %s, Iron: %s, Vitamin A: %s",
                        data.calories.toString(),
                        data.vitamin_protein.toString(),
                        data.vitamin_iron.toString(),
                        data.vitamin_a.toString())


                btnBack.setOnClickListener { listener.onClick(Actions.VIEW, data) }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MealRecipe>() {
        override fun areItemsTheSame(oldItem: MealRecipe, newItem: MealRecipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MealRecipe, newItem: MealRecipe): Boolean {
            return oldItem.name == newItem.name && oldItem.details == newItem.details
        }
    }
}