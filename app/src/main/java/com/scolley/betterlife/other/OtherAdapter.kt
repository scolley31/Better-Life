package com.scolley.betterlife.other

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Category
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.databinding.ItemOtherGridBinding
import kotlinx.android.synthetic.main.item_home_grid.view.*
import kotlinx.android.synthetic.main.item_other_grid.view.*

class OtherAdapter(val viewModel: OtherViewModel, private val onClickListener: OnClickListener) :
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

    override fun onBindViewHolder(holder: OtherPlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(getItem(position), viewModel)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan)
        }
        holder.itemView.button_addOthersTask.setOnClickListener {
            viewModel.addToOtherTask(plan)
        }

        holder.itemView.image_other_category.setImageResource(
                when (plan.category) {
                    Category.STUDY.category -> R.drawable._28_learning
                    Category.EXERCISE.category -> R.drawable._10_training
                    Category.HABIT.category -> R.drawable._33_skill
                    Category.OTHER.category -> R.drawable._22_puzzle
                    else -> R.drawable._28_learning
                }
        )

    }

    class OnClickListener(val clickListener: (plan: Plan) -> Unit) {
        fun onClick(plan: Plan) = clickListener(plan)
    }
}