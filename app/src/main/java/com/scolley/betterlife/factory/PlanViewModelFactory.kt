package com.scolley.betterlife.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.PlanForShow
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.timer.TimerViewModel

@Suppress("UNCHECKED_CAST")
class PlanViewModelFactory(
        private val planRepository: PlanRepository,
        private val plan:Plan?,
        private val planTeam:PlanForShow?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(TimerViewModel::class.java) ->
                        TimerViewModel(planRepository, plan, planTeam)


                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}

