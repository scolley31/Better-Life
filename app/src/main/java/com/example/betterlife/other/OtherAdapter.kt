package com.example.betterlife.other

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.betterlife.data.Plan
import com.example.betterlife.databinding.ItemHomeGridBinding
import com.example.betterlife.databinding.ItemOtherGridBinding
import com.example.betterlife.home.item.HomeItemViewModel
import kotlinx.android.synthetic.main.item_other_grid.view.*

class OtherAdapter(val viewModel: OtherViewModel, val onClickListener: OnClickListener) :
        ListAdapter<Plan, OtherAdapter.OtherPlanViewHolder>(DiffCallback) {

    class OtherPlanViewHolder(private var binding: ItemOtherGridBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(otherPlan: Plan, viewModel: OtherViewModel) {
            binding.otherPlan = otherPlan
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherPlanViewHolder {
        return OtherPlanViewHolder(ItemOtherGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Product]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Product]
     */
    override fun onBindViewHolder(holder: OtherPlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(getItem(position), viewModel)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan)
        }
        holder.itemView.button_addOthersTask.setOnClickListener {
            viewModel.addToOtherTask(plan)
        }

    }

    class OnClickListener(val clickListener: (plan : Plan) -> Unit) {
        fun onClick(plan : Plan) = clickListener(plan)
    }
}