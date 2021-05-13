package com.example.betterlife.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.betterlife.data.Plan
import com.example.betterlife.databinding.ItemHomeGridBinding

class HomeItemAdapter(val viewModel: HomeItemViewModel) :
        ListAdapter<Plan, HomeItemAdapter.PlanViewHolder>(DiffCallback) {

    class PlanViewHolder(private var binding: ItemHomeGridBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Plan, viewModel: HomeItemViewModel) {

            binding.plan = plan
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Plan>() {
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return (oldItem.name == newItem.name)
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem.createdTime == newItem.createdTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        return PlanViewHolder(ItemHomeGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}
