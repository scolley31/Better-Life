package com.scolley.betterlife.timer.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Completed
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.Result
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.newwork.LoadApiStatus
import com.scolley.betterlife.util.Logger
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class TimerItemViewModel(private val repository: PlanRepository): ViewModel() {

    val _timer = MutableLiveData<Plan>()

    val timer: LiveData<Plan>
        get() = _timer

    val selectedTypeRadio = MutableLiveData<Int>()

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

    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: MutableLiveData<Boolean>
        get() = _navigateToHome

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
        selectedTypeRadio.value = R.id.radio_count_target
    }

    fun navigateToHome() {
        _navigateToHome.value = true
    }

    fun leaveTimer() {
        _leaveTimer.value = true
    }

    fun checkTaskDone() {

        _status.value = LoadApiStatus.LOADING

        coroutineScope.launch {
            var completed = repository.getCompleted(_timer.value!!.id, FirebaseAuth.getInstance().currentUser!!.uid)
            var completedList = when (completed) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    completed.data
                }
                is Result.Fail -> {
                    _error.value = completed.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = completed.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            var sum = 0
            if (completedList != null) {
                for (i in completedList.indices) {
                    sum += completedList[i].daily
                }
            }
            if (sum >= _timer.value!!.target) {
                /**
                 * When task is finish , what to do?
                 */
            }
        }
    }


    fun sendCompleted() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val newCompleted = Completed(
                    user_id = FirebaseAuth.getInstance().currentUser!!.uid,
                    daily = when(selectedTypeRadio.value){
                        R.id.radio_count_target -> dailyTaskTarget.value!!.minus(dailyTaskRemained.value!!).plus(1)
                        R.id.radio_count_noLimit -> dailyTaskRemained.value!!
                        else -> dailyTaskTarget.value!!.minus(dailyTaskRemained.value!!).plus(1)},
                    completed = completed.value!!,
                    date = Calendar.getInstance().timeInMillis
            )

            Log.d("test","newCompleted = $newCompleted")

            when (val result = timer.value?.let { repository.sendCompleted(newCompleted, it.id) }) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    checkTaskDone()
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
            navigateToHome()
        }
    }

}