package com.example.betterlife.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.Completed
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Result
import com.example.betterlife.data.User
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.newwork.LoadApiStatus
import com.example.betterlife.util.Logger
import com.example.betterlife.util.TimeConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(private val repository: PlanRepository, private val arguments: String):ViewModel() {

    private val _plans = MutableLiveData<List<Plan>>()

    val plans: LiveData<List<Plan>>
        get() = _plans

    private val _user = repository.getUser(arguments)

    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    val singlePlanCompleted = MutableLiveData<List<Completed>?>()

    val taskDoneNumber = MutableLiveData<Int>()

    val taskPercent = MutableLiveData<Int>()

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

        getPlanResult()

    }

    fun getPlanResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.getPlanResult()
            Log.d("test","result = $result")

            _plans.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
//                }
                }
            }
            getCompleted()
            Log.d("test","plan = ${_plans.value}")
            _refreshStatus.value = false
        }
    }

    fun getCompleted(){

        coroutineScope.launch {
            for (i in _plans.value!!.indices) {
                var completed = repository.getCompleted(_plans.value!![i].id, arguments)
                singlePlanCompleted.value = when(completed) {
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
                singlePlanCompleted.value?.let {
                    var sum : Int = 0
                    for (j in singlePlanCompleted.value!!.indices){
                        sum += singlePlanCompleted.value!![j].daily
                        checkTodayDone(j,i)
                    }

                    _plans.value!![i].progressTime = sum/60

                    if (sum>= _plans.value!![i].target*60) {
                        _plans.value!![i].taskDone = true
                    }
                }
            }
            val filter = _plans.value!!.filter {
                it.taskDone == false
            }
            _plans.value = filter
            checkTodayDoneNumber()
            _plans.value = _plans.value

        }
    }

    fun checkTodayDone(j: Int,i : Int) {

        val today = TimeConverters.timestampToDate(Calendar.getInstance().timeInMillis, Locale.TAIWAN)
        val completedDay = TimeConverters.timestampToDate(singlePlanCompleted.value!![j].date, Locale.TAIWAN)

        if (completedDay == today) {
            _plans.value!![i].todayDone = true
        }
    }

    fun checkTodayDoneNumber() {
        var doneNumber = 0
        var totalNumber = 0
        for (i in plans.value!!.indices) {
            totalNumber += 1
            if (plans.value!![i].todayDone == true)
                doneNumber += 1
        }
        taskDoneNumber.value = doneNumber
        if(totalNumber != 0) {
            taskPercent.value = doneNumber * 100 / totalNumber
//            Log.d("test", "taskDoneNumber.value  = ${taskDoneNumber.value}")
//            Log.d("test", "taskPercent.value  = ${taskPercent.value}")
        }
    }

}