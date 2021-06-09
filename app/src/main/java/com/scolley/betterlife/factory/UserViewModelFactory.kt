package com.scolley.betterlife.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(
        private val planRepository: PlanRepository,
        private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {

                    isAssignableFrom(HomeViewModel::class.java) ->
                        HomeViewModel(planRepository, userId)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}