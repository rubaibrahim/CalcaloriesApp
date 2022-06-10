package com.gp2.calcalories.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gp2.calcalories.R
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.databinding.ItemPlanBinding
import com.gp2.calcalories.remote.model.plan.entity.UserPlan
import java.util.*


class PLanAdapter(private val listener: GeneralListener) : ListAdapter<UserPlan, RecyclerView.ViewHolder>(
    DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemPlanBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserPlan) {
            binding.apply {

                txtName.text = data.meal_type?.name
                txtCalories.text = String.format(Locale.ENGLISH,"%d/%d kcal",
                    data.totals?.used_calories ?: 0, data.calories)


                val progress = data.totals?.used_calories ?: 0
                if(progress > 0) {
                    progressBar.max = data.calories
                    progressBar.progress = progress
                    progressBar.progressTintList = getVitaminColor(progressBar,
                        data.totals?.used_calories ?: 0, data.calories)
                }/*else {
                    progressBar.max = 1
                    progressBar.progress = 1
                    progressBar.progressTintList = ColorStateList.valueOf(progressBar.context
                        .resources.getColor(R.color.lightGray))
                }*/

                // Add meals
                val adapter = PLanMealAdapter(listener)
                recyclerView.adapter = adapter
                adapter.submitList(data.meals)


                btnAdd.setOnClickListener { listener.onClick(Actions.ADD, data) }
                btnBack.setOnClickListener { listener.onClick(Actions.VIEW, data) }
            }
        }

        private fun getVitaminColor(pB:ProgressBar,used:Int, total:Int) : ColorStateList{
            if(used >= 0 && used < (total / 100 * 30)){
                return ColorStateList.valueOf(pB.context.resources.getColor(R.color.red2))
            }else if(used >= (total / 100 * 30) && used < total){
                return ColorStateList.valueOf(pB.context.resources.getColor(R.color.yallow))
            }else {
                return ColorStateList.valueOf(pB.context.resources.getColor(R.color.green))
            }
        }
    }



    companion object DiffCallback : DiffUtil.ItemCallback<UserPlan>() {
        override fun areItemsTheSame(oldItem: UserPlan, newItem: UserPlan): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserPlan, newItem: UserPlan): Boolean {
            return oldItem.calories == newItem.calories
                    && oldItem.vitamin_protein == newItem.vitamin_protein
                    && oldItem.vitamin_iron == newItem.vitamin_iron
                    && oldItem.vitamin_a == newItem.vitamin_a
                    && oldItem.totals == newItem.totals
        }
    }
}