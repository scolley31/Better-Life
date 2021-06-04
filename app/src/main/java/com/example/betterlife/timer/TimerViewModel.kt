package com.example.betterlife.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.data.Plan
import com.example.betterlife.data.PlanForShow
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.timer.item.TimerStatus
import java.util.*

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