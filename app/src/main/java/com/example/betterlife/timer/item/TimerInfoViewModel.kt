package com.example.betterlife.timer.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanRepository

class TimerInfoViewModel(private val repository: PlanRepository): ViewModel() {

    val _info = MutableLiveData<Plan>()

    val info: LiveData<Plan>
        get() = _info

}