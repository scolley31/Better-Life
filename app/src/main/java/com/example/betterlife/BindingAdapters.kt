package com.example.betterlife

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.betterlife.data.Plan
import com.example.betterlife.home.item.HomeItemAdapter

@BindingAdapter("plan")
fun bindRecyclerViewWithPlan(recyclerView: RecyclerView, plan: List<Plan>?) {
    plan?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is HomeItemAdapter -> submitList(it)
                }
            }
        }
    }
