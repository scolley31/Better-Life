package com.scolley.betterlife.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scolley.betterlife.MainViewMode
import com.scolley.betterlife.addtask.AddTaskViewModel
import com.scolley.betterlife.login.LoginViewModel
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.home.item.HomeDoneViewModel
import com.scolley.betterlife.home.item.HomeItemViewModel
import com.scolley.betterlife.home.item.HomeTeamViewModel
import com.scolley.betterlife.other.OtherViewModel
import com.scolley.betterlife.timer.item.TimerInfoDateViewModel
import com.scolley.betterlife.timer.item.TimerInfoViewModel
import com.scolley.betterlife.timer.item.TimerItemViewModel
import com.scolley.betterlife.timer.team.TimerTeamDateViewModel
import com.scolley.betterlife.timer.team.TimerTeamItemViewModel
import com.scolley.betterlife.timer.team.TimerTeamViewModel

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

                    isAssignableFrom(HomeDoneViewModel::class.java) ->
                        HomeDoneViewModel(repository)

                    isAssignableFrom(HomeTeamViewModel::class.java) ->
                        HomeTeamViewModel(repository)

                    isAssignableFrom(TimerItemViewModel::class.java) ->
                        TimerItemViewModel(repository)

                    isAssignableFrom(TimerInfoViewModel::class.java) ->
                        TimerInfoViewModel(repository)

                    isAssignableFrom(TimerInfoDateViewModel::class.java) ->
                        TimerInfoDateViewModel(repository)

                    isAssignableFrom(TimerTeamItemViewModel::class.java) ->
                        TimerTeamItemViewModel(repository)

                    isAssignableFrom(TimerTeamDateViewModel::class.java) ->
                        TimerTeamDateViewModel(repository)

                    isAssignableFrom(TimerTeamViewModel::class.java) ->
                        TimerTeamViewModel(repository)

                    isAssignableFrom(AddTaskViewModel::class.java) ->
                        AddTaskViewModel(repository)

                    isAssignableFrom(MainViewMode::class.java) ->
                        MainViewMode(repository)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}




