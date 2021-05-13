package com.example.betterlife.ext

import androidx.fragment.app.Fragment
import com.example.betterlife.PlanApplication
import com.example.betterlife.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as PlanApplication).repository
    return ViewModelFactory(repository)
}

