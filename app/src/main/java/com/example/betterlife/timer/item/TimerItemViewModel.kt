package com.example.betterlife.timer.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.Completed
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.newwork.LoadApiStatus
import com.example.betterlife.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TimerItemViewModel(private val repository: PlanRepository): ViewModel() {

    val _timer = MutableLiveData<Plan>()

    val timer: LiveData<Plan>
        get() = _timer

//    private val _completedInfo = MutableLiveData<Completed>()
//
//    val completedInfo: LiveData<Completed>
//        get() = _completedInfo

    private val _leaveTimer = MutableLiveData<Boolean>()

    val leaveTimer: LiveData<Boolean>
        get() = _leaveTimer

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    val dailyTaskRemained = MutableLiveData<Int>()

    val dailyTaskTarget = MutableLiveData<Int>()

    val completed = MutableLiveData<Boolean>()

    var timeStatus =  MutableLiveData<TimerStatus>()

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
        timeStatus.value = TimerStatus.Stopped
        completed.value = false
    }

    fun leaveTimer() {
        _leaveTimer.value = true
    }

    fun sendCompleted() {
        coroutineScope.launch {

            val newCompleted = Completed(
                    user_id = "Scolley",
                    daily = dailyTaskTarget.value!!.minus(dailyTaskRemained.value!!).plus(1),
                    completed = completed.value!!,
                    date = Calendar.getInstance().timeInMillis
            )

            Log.d("test","newCompleted = $newCompleted")

            when (val result = timer.value?.let { repository.sendCompleted(newCompleted, it.id) }) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    leaveTimer()
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }

        leaveTimer
    }

}