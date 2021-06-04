package com.example.betterlife.ext

import androidx.fragment.app.Fragment
import com.example.betterlife.PlanApplication
import com.example.betterlife.data.Plan
import com.example.betterlife.data.PlanForShow
import com.example.betterlife.factory.PlanViewModelFactory
import com.example.betterlife.factory.UserViewModelFactory
import com.example.betterlife.factory.ViewModelFactory

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

