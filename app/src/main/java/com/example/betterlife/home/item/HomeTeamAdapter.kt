package com.example.betterlife.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.betterlife.R
import com.example.betterlife.data.Category
import com.example.betterlife.data.Plan
import com.example.betterlife.data.PlanForShow
import com.example.betterlife.databinding.ItemHomeGridBinding
import com.example.betterlife.databinding.ItemHomeTeamGridBinding
import kotlinx.android.synthetic.main.item_home_grid.view.*

class HomeTeamAdapter(val viewModel: HomeTeamViewModel,val onClickListener: OnClickListener) :
        ListAdapter<PlanForShow, HomeTeamAdapter.PlanViewHolder>(DiffCallback) {

    class PlanViewHolder(private var binding: ItemHomeTeamGridBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: PlanForShow, viewModel: HomeTeamViewModel) {
            binding.plan = plan
            binding.viewModel = viewModel

            when(plan.category) {
                Category.STUDY.category -> binding.imageIconTask
            }


            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<PlanForShow>() {
        override fun areItemsTheSame(oldItem: PlanForShow, newItem: PlanForShow): Boolean {
            return (oldItem.name == newItem.name)
        }

        override fun areContentsTheSame(oldItem: PlanForShow, newItem: PlanForShow): Boolean {
            return oldItem.createdTime == newItem.createdTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        return PlanViewHolder(ItemHomeTeamGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Product]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Product]
     */
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = getItem(position)
        holder.bind(getItem(position), viewModel)
        holder.itemView.image_icon_task.setImageResource(
                when(plan.category){
                    Category.STUDY.category -> R.drawable._28_learning
                    Category.EXERCISE.category -> R.drawable._10_training
                    Category.HABIT.category -> R.drawable._33_skill
                    Category.OTHER.category -> R.drawable._22_puzzle
                    else ->  R.drawable._28_learning
                }
        )

        holder.itemView.setOnClickListener {
            onClickListener.onClick(plan)
        }
//        holder.itemView.button_delete.setOnClickListener {
//            viewModel.deletePlan(plan)
//        }

    }

    class OnClickListener(val clickListener: (plan : PlanForShow) -> Unit) {
        fun onClick(plan : PlanForShow) = clickListener(plan)
    }
}