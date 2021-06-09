package com.scolley.betterlife.ext

import androidx.fragment.app.Fragment
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.PlanForShow
import com.scolley.betterlife.factory.PlanViewModelFactory
import com.scolley.betterlife.factory.UserViewModelFactory
import com.scolley.betterlife.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as PlanApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(plan: Plan?, planTeam: PlanForShow?): PlanViewModelFactory {
    val repository = (requireContext().applicationContext as PlanApplication).repository
    return PlanViewModelFactory(repository, plan, planTeam)
}


fun Fragment.getVmFactory(userId: String): UserViewModelFactory {
    val repository = (requireContext().applicationContext as PlanApplication).repository
    return UserViewModelFactory(repository, userId)
}

