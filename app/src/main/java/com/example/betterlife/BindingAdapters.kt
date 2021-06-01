package com.example.betterlife

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.betterlife.data.CurrentFragmentType
import com.example.betterlife.data.Plan
import com.example.betterlife.data.PlanForShow
import com.example.betterlife.home.item.HomeDoneAdapter
import com.example.betterlife.home.item.HomeItemAdapter
import com.example.betterlife.home.item.HomeTeamAdapter
import com.example.betterlife.other.OtherAdapter

@BindingAdapter("plans")
fun bindRecyclerViewWithPlan(recyclerView: RecyclerView, plan: List<Plan>?) {
    plan?.let {
        recyclerView.adapter?.apply {
            when (this) {
                        is HomeDoneAdapter -> {
                            notifyDataSetChanged()
                            submitList(it)
                        }
                        is HomeItemAdapter -> {
                            notifyDataSetChanged()
                            submitList(it)
                        }
                        is OtherAdapter -> {
                            notifyDataSetChanged()
                            submitList(it)
                        }
                    }
                }
            }
    }

@BindingAdapter("plansForShow")
fun bindRecyclerViewWithPlanForShow(recyclerView: RecyclerView, plan: List<PlanForShow>?) {
    plan?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is HomeTeamAdapter -> {
                    notifyDataSetChanged()
                    submitList(it)
                }
            }
        }
    }
}

@BindingAdapter("toolbarVisibility")
fun bindToolbarVisibility(view: View, fragment: CurrentFragmentType) {
    view.visibility =
            when (fragment) {
                CurrentFragmentType.LOGIN -> View.GONE
                CurrentFragmentType.TIMER -> View.GONE

                else -> View.VISIBLE
            }
}

@BindingAdapter("bottomNavVisibility")
fun bindBottomNavVisibility(view: View, fragment: CurrentFragmentType) {
    view.visibility =
            when (fragment) {
                CurrentFragmentType.LOGIN -> View.GONE
                CurrentFragmentType.TIMER -> View.GONE

                else -> View.VISIBLE
            }
}


