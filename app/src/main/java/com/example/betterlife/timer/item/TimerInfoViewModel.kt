package com.example.betterlife.timer.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.data.Completed
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanRepository

class TimerInfoViewModel(private val repository: PlanRepository): ViewModel() {

    val _info = MutableLiveData<Plan>()

    val info: LiveData<Plan>
        get() = _info

    private val _completed = MutableLiveData<Completed>()

    val completed: LiveData<Completed>
        get() = _completed


    private val _leaveTimer = MutableLiveData<Boolean>()

    val leaveTimer: LiveData<Boolean>
        get() = _leaveTimer


    fun leaveTimer() {
        _leaveTimer.value = true
    }

}