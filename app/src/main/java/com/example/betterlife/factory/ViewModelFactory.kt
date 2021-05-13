package com.example.betterlife.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.betterlife.MainViewMode
import com.example.betterlife.data.Source.PlanRepository
import com.example.betterlife.home.HomeViewModel
import com.example.betterlife.home.item.HomeItemViewModel
import com.example.betterlife.other.OtherFragment
import com.example.betterlife.other.OtherViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
        private val repository: PlanRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(OtherViewModel::class.java) ->
                        OtherViewModel(repository)

                    isAssignableFrom(HomeItemViewModel::class.java) ->
                        HomeItemViewModel(repository)

                    isAssignableFrom(MainViewMode::class.java) ->
                        MainViewMode(repository)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}
