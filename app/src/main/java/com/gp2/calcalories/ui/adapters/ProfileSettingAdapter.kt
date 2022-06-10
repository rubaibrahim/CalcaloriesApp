package com.gp2.calcalories.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gp2.calcalories.databinding.ItemProfileSettingBinding
import com.gp2.calcalories.common.listner.GeneralListener
import com.gp2.calcalories.common.model.ProfileSetting

class ProfileSettingAdapter(private val itemList: MutableList<ProfileSetting>,private val listener: GeneralListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ItemProfileSettingBinding.inflate(inflater, parent, false)
        return ViewHolder(ItemProfileSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(itemList[position])
    }

    inner class ViewHolder(val binding: ItemProfileSettingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: ProfileSetting) {

            binding.record = record
            binding.btn.setOnClickListener { listener.onClick(record.id) }
            binding.executePendingBindings()
        }
    }
}