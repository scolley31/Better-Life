package com.scolley.betterlife.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Category
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.databinding.ItemHomeDoneGridBinding
import kotlinx.android.synthetic.main.item_home_grid.view.*

class HomeDoneAdapter(val viewModel: HomeDoneViewModel,val onClickListener: OnClickListener) :
        ListAdapter<Plan, HomeDoneAdapter.PlanViewHolder>(DiffCallback) {

    class PlanViewHolder(private var binding: ItemHomeDoneGridBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Plan, viewModel: HomeDoneViewModel) {

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
        return PlanViewHolder(ItemHomeDoneGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Product]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Product]
     */
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(getItem(position), viewModel)
//        holder.itemView.setOnClickListener {
//            onClickListener.onClick(plan)
//        }
        holder.itemView.image_icon_task.setImageResource(
            when(plan.category){
                Category.STUDY.category -> R.drawable._28_learning
                Category.EXERCISE.category -> R.drawable._10_training
                Category.HABIT.category -> R.drawable._33_skill
                Category.OTHER.category -> R.drawable._22_puzzle
                else ->  R.drawable._28_learning
            }
        )

    }

    class OnClickListener(val clickListener: (plan : Plan) -> Unit) {
        fun onClick(plan : Plan) = clickListener(plan)
    }
}
