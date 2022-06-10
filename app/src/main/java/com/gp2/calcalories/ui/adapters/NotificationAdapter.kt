package com.gp2.calcalories.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gp2.calcalories.R
import com.gp2.calcalories.common.enums.Actions
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.databinding.ItemNotificationBinding
import com.gp2.calcalories.remote.model.user.entity.UserNotification

class NotificationAdapter(private val listener: GeneralListener) :
    ListAdapter<UserNotification, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserNotification) {
            binding.apply {
                txtTitle.text = data.title
                txtMessage.text = data.message

                if(data.is_read == 1){
                    binding.card.setCardBackgroundColor(binding.card.resources.getColor(R.color.white))
                }else {
                    binding.card.setCardBackgroundColor(binding.card.resources.getColor(R.color.lightGray))
                }

                if(data.type == 1){
                    btnRecipes.visibility = View.GONE
                    btnBack.setOnClickListener { listener.onClick(Actions.VIEW, data) }
                }else {
                    btnRecipes.visibility = View.VISIBLE
                    btnRecipes.setOnClickListener { listener.onClick(Actions.VIEW, data) }
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<UserNotification>() {
        override fun areItemsTheSame(oldItem: UserNotification, newItem: UserNotification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserNotification, newItem: UserNotification): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.message == newItem.message
                    && oldItem.is_read == newItem.is_read
                    && oldItem.date == newItem.date
        }
    }
}