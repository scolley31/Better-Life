package com.scolley.betterlife.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.PlanForShow
import com.scolley.betterlife.data.source.PlanRepository

class TimerViewModel(private val planRepository: PlanRepository,
                     private val arguments: Plan?,
                     private val argumentsTeam: PlanForShow?
): ViewModel() {

    private val _plan = MutableLiveData<Plan?>().apply {
        value = arguments
    }

    val plan: LiveData<Plan?>
        get() = _plan

    private val _planTeam = MutableLiveData<PlanForShow?>().apply {
        value = argumentsTeam
    }

    val planTeam: LiveData<PlanForShow?>
        get() = _planTeam




}