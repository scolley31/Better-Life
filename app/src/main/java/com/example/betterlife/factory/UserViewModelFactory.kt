package com.example.betterlife.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.home.HomeViewModel

class UserViewModelFactory(
        private val repository: PlanRepository,
        private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {

                    isAssignableFrom(HomeViewModel::class.java) ->
                        HomeViewModel(repository, userId)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}