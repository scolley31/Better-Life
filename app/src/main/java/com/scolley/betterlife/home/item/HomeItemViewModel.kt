package com.scolley.betterlife.home.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Completed
import com.scolley.betterlife.data.Plan
import com.scolley.betterlife.data.User
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.newwork.LoadApiStatus
import com.scolley.betterlife.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.scolley.betterlife.data.Result
import com.scolley.betterlife.util.TimeConverters
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class HomeItemViewModel(private val repository: PlanRepository):ViewModel() {

    val ONE_DAY_MILLI_SECOND : Int = 86400 * 1000

    private val _plans = MutableLiveData<List<Plan>>()

    val plans: LiveData<List<Plan>>
        get() = _plans

    var livePlans = MutableLiveData<List<Plan>>()

    private var _allDaily = MutableLiveData<List<Int>?>()

    val allDaily: LiveData<List<Int>?>
        get() = _allDaily

    private val _navigateToTimer = MutableLiveData<Plan?>()

    val navigateToTimer: MutableLiveData<Plan?>
        get() = _navigateToTimer

    val _user = MutableLiveData<User>()

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

    val singlePlanCompleted = MutableLiveData<List<Completed>>()

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


    fun getLivePlanResult() {
        livePlans = repository.getLivePlanResult()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
        Log.d("test","livePlans = ${livePlans.value}")
    }

    fun deletePlan(plan: Plan) {

        coroutineScope.launch {
            if (plan.members.size == 1) {

                val result = repository.deleteTask(plan.id)
                Log.d("result","deleteTask result = $result")
                getPlanResult()
            } else {

                val result = repository.deleteUserOngoingTask(user.value!!.userId,plan.id)
                Log.d("result","deleteUserOngoingTask result = $result")
                getPlanResult()

            }
        }

    }

    fun getPlanResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.getPlanResult()
//            Log.d("test","result = $result")

            _plans.value = when (result) {
                is Result.Success -> {
                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
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
            _refreshStatus.value = false
            getCompleted()
        }
    }


    fun getCompleted(){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            for (i in _plans.value!!.indices) {
                var completed = repository.getCompleted(_plans.value!![i].id, user.value!!.userId)
                singlePlanCompleted.value = when(completed) {
                    is Result.Success -> {
                        _error.value = null
//                        _status.value = LoadApiStatus.DONE
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

                when (_plans.value!![i].dueDate) {
                    -1L  -> singleTarget(i)
                    else -> singleDueData(i)
                }
//                Log.d("test","_plans.value!![i] = ${_plans.value!![i]}")

            }

            val filter = _plans.value!!.filter {
                !it.taskDone && !it.group
            }

            _plans.value = filter
            _plans.value = _plans.value

            _status.value = LoadApiStatus.DONE
        }
    }


    fun singleTarget(i: Int) {
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


    fun singleDueData(i: Int) {
        var completedDay : Int = 0
        var totalCompletedDay: Long = (_plans.value!![i].dueDate - _plans.value!![i].createdTime) / ONE_DAY_MILLI_SECOND
        for (j in singlePlanCompleted.value!!.indices){
            if (singlePlanCompleted.value!![j].completed){
                completedDay++
            }
            checkTodayDone(j,i)
        }
//        Log.d("test","totalCompletedDay = $totalCompletedDay")
//        Log.d("test","completedDay = $completedDay")

        _plans.value!![i].progressTime = completedDay
        _plans.value!![i].target = when(totalCompletedDay) {
            0L -> 1
            else -> totalCompletedDay.toInt()
        }

        val today = Calendar.getInstance().timeInMillis
        val dueDate = _plans.value!![i].dueDate
        Log.d("today","today = $today")
        Log.d("today","dueDate = $dueDate")
        if (dueDate <= today) {
            _plans.value!![i].taskDone = true
        }
//        Log.d("test","_plans.value!![i].taskDone = ${_plans.value!![i].taskDone}")

    }


    fun checkTodayDone(j: Int,i : Int) {

        val today = TimeConverters.timestampToDate(Calendar.getInstance().timeInMillis, Locale.TAIWAN)
        val completedDay = TimeConverters.timestampToDate(singlePlanCompleted.value!![j].date, Locale.TAIWAN)
        val completedUser = singlePlanCompleted.value!![j].user_id
//        Log.d("test1","completedUser = $completedUser")
//        Log.d("test1","FirebaseAuth.getInstance().currentUser!!.uid = ${FirebaseAuth.getInstance().currentUser!!.uid}")
        if (completedDay == today && completedUser == FirebaseAuth.getInstance().currentUser!!.uid) {
            _plans.value!![i].todayDone = true
        }
//        Log.d("test1","_plans.value!![i].todayDone = ${_plans.value!![i].todayDone}")
//        Log.d("test1","_plans.value!![i] = ${_plans.value!![i]}")

    }

    fun navigateTimer(plan: Plan) {
        _navigateToTimer.value = plan
    }


    fun deleteNavigateTimer() {
        _navigateToTimer.value = null
    }


}