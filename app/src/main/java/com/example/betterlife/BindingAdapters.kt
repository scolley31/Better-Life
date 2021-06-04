package com.example.betterlife

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.betterlife.data.CurrentFragmentType
import com.example.betterlife.data.Plan
import com.example.betterlife.data.PlanForShow
import com.example.betterlife.home.item.HomeDoneAdapter
import com.example.betterlife.home.item.HomeItemAdapter
import com.example.betterlife.home.item.HomeTeamAdapter
import com.example.betterlife.newwork.LoadApiStatus
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


@BindingAdapter("setupApiStatus")
fun bindApiStatus(view: ProgressBar, status: LoadApiStatus?) {
    when (status) {
        LoadApiStatus.LOADING -> view.visibility = View.VISIBLE
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> view.visibility = View.GONE
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

@BindingAdapter("glide")
fun glidingImage(imageView: ImageView, url: String?) {
    url?.let {
        val uri = it.toUri().buildUpon().build()

        Glide.with(imageView.context)
            .load(uri).apply(
                RequestOptions()
                    .placeholder(R.drawable._01_arm_wrestling)
                    .error(R.drawable._01_arm_wrestling)
            )
            .into(imageView)
    }
}


