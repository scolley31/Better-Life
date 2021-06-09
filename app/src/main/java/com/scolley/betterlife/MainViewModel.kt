package com.scolley.betterlife

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.data.CurrentFragmentType
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainViewMode(private val repository: PlanRepository): ViewModel() {

    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
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