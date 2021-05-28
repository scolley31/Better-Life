package com.example.betterlife.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.betterlife.MainViewMode
import com.example.betterlife.addtask.AddTaskViewModel
import com.example.betterlife.login.LoginViewModel
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.home.HomeViewModel
import com.example.betterlife.home.item.HomeDoneViewModel
import com.example.betterlife.home.item.HomeItemViewModel
import com.example.betterlife.other.OtherViewModel
import com.example.betterlife.timer.item.TimerInfoViewModel
import com.example.betterlife.timer.item.TimerItemViewModel

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

                    isAssignableFrom(LoginViewModel::class.java) ->
                        LoginViewModel(repository)

                    isAssignableFrom(HomeViewModel::class.java) ->
                        HomeViewModel(repository)

                    isAssignableFrom(HomeDoneViewModel::class.java) ->
                        HomeDoneViewModel(repository)

                    isAssignableFrom(TimerItemViewModel::class.java) ->
                        TimerItemViewModel(repository)

                    isAssignableFrom(TimerInfoViewModel::class.java) ->
                        TimerInfoViewModel(repository)

                    isAssignableFrom(AddTaskViewModel::class.java) ->
                        AddTaskViewModel(repository)

                    isAssignableFrom(MainViewMode::class.java) ->
                        MainViewMode(repository)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}




