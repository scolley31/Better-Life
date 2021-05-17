package com.example.betterlife.addtask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AddTaskViewModel(private val repository: PlanRepository): ViewModel() {

    private val _newTask = MutableLiveData<Plan?>()

    val newTask: MutableLiveData<Plan?>
        get() = _newTask

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
    }

}