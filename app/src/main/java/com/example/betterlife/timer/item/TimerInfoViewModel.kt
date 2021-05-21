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
import com.example.betterlife.util.TimeConverters
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TimerInfoViewModel(private val repository: PlanRepository): ViewModel() {

    val _info = MutableLiveData<Plan>()

    val info: LiveData<Plan>
        get() = _info

    private val _completedTest = MutableLiveData<List<Completed>>()

    val completedTest: LiveData<List<Completed>>
        get() = _completedTest

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

    val averageDailyTime = MutableLiveData<Int>()

    val xTitle = mutableListOf<String>()
    val entries : MutableList<BarEntry> = arrayListOf()
    val label: MutableList<String> = mutableListOf()

    val forPrintChat = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun leaveTimer() {
        _leaveTimer.value = true
    }

    init {
        getCompletedForChart()
    }

    fun getCompletedForChart() {

        coroutineScope.launch {

            var completed = repository.getCompleted(_info.value!!.id, "Scolley")
            _completedTest.value = when(completed) {
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
            var number = 0
            for ( i in completedTest.value!!.indices) {

                xTitle.add(completedTest.value!![i].date.let {
                    TimeConverters.timestampToDate(it, Locale.TAIWAN)
                })

                label.add(completedTest.value!![i].date.let {
                    TimeConverters.timestampToDate(it, Locale.TAIWAN)
                })

                entries.add(BarEntry(i.toFloat(), completedTest.value!![i].daily.toFloat()))

                sum += completedTest.value!![i].daily
                number += 1
            }

            averageDailyTime.value = sum/number
            forPrintChat.value = true


            Log.d("test","xTitle = $xTitle")
            Log.d("test","label = $label")
            Log.d("test","entries = $entries")
        }
    }

}