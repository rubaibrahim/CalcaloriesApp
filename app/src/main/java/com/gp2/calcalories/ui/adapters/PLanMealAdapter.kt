package com.gp2.calcalories.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.databinding.ItemPlanMealBinding
import com.gp2.calcalories.remote.model.plan.entity.PlanMeal
import java.util.*

class PLanMealAdapter(private val listener: GeneralListener) : ListAdapter<PlanMeal, RecyclerView.ViewHolder>(
    DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemPlanMealBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind( getItem(position))
    }

    inner class ViewHolder(val binding: ItemPlanMealBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PlanMeal) {
            binding.apply {
                txtName.text = data.name
                txtCalories.text = String.format(Locale.ENGLISH,"%d kcal", data.calories)
                btnDelete.setOnClickListener { listener.onClick(Actions.DELETE, data) }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PlanMeal>() {
        override fun areItemsTheSame(oldItem: PlanMeal, newItem: PlanMeal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlanMeal, newItem: PlanMeal): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.calories == newItem.calories
                    && oldItem.vitamin_protein == newItem.vitamin_protein
                    && oldItem.vitamin_iron == newItem.vitamin_iron
                    && oldItem.vitamin_a == newItem.vitamin_a
        }
    }
}