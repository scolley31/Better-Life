package com.example.betterlife.home.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.*
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.newwork.LoadApiStatus
import com.example.betterlife.util.Logger
import com.example.betterlife.util.TimeConverters
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class HomeTeamViewModel(private val repository: PlanRepository): ViewModel() {

    val ONE_DAY_MILLI_SECOND : Int = 86400 * 1000

    private val _plans = MutableLiveData<List<Plan>>()

    val plans: LiveData<List<Plan>>
        get() = _plans

    private val _plansForShow = MutableLiveData<List<PlanForShow>>()

    val plansForShow: LiveData<List<PlanForShow>>
        get() = _plansForShow

    private val _groups = MutableLiveData<List<Groups>>()

    val groups: LiveData<List<Groups>>
        get() = _groups

    var livePlans = MutableLiveData<List<Plan>>()

    private var _allDaily = MutableLiveData<List<Int>?>()

    val allDaily: LiveData<List<Int>?>
        get() = _allDaily

    private val _navigateToTimer = MutableLiveData<PlanForShow?>()

    val navigateToTimer: MutableLiveData<PlanForShow?>
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

    val ownPlanCompleted = MutableLiveData<List<Completed>>()

    val partnerPlanCompleted = MutableLiveData<List<Completed>>()

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

        getGroupPlanResult()

    }

    fun getGroupPlanResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING
            val result = repository.getGroupPlanResult()
            Log.d("test","getGroupPlanResult = $result")

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
            getCompletedAndGroup()
            _refreshStatus.value = false
        }
    }



    fun getCompletedAndGroup() {

        coroutineScope.launch {
            for (i in _plans.value!!.indices) {

                var group = repository.getGroup(_plans.value!![i].id, user.value!!.userId)
                Log.d("group","group = $group")
                _groups.value = when (group) {
                    is Result.Success -> {
                        _error.value = null
                        _status.value = LoadApiStatus.DONE
                        group.data

                    }
                    is Result.Fail -> {
                        _error.value = group.error
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                    is Result.Error -> {
                        _error.value = group.exception.toString()
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                    else -> {
                        _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                        _status.value = LoadApiStatus.ERROR
                        null
                    }
                }

                if ( _groups.value!![0].membersID[0] == user.value!!.userId) {

                    var completedOne = repository.getCompleted(_plans.value!![i].id, _groups.value!![0].membersID[0])
//                    Log.d("group","completedOne = $completedOne"+ "i = $i")
                    ownPlanCompleted.value = when (completedOne) {
                        is Result.Success -> {
                            _error.value = null
                            _status.value = LoadApiStatus.DONE
                            completedOne.data
                        }
                        is Result.Fail -> {
                            _error.value = completedOne.error
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        is Result.Error -> {
                            _error.value = completedOne.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        else -> {
                            _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                    }

                    var completedTwo = repository.getCompleted(_plans.value!![i].id, _groups.value!![0].membersID[1])
//                Log.d("group","completedTwo = $completedTwo"+ "i = $i")
                    partnerPlanCompleted.value = when (completedTwo) {
                        is Result.Success -> {
                            _error.value = null
                            _status.value = LoadApiStatus.DONE
                            completedTwo.data
                        }
                        is Result.Fail -> {
                            _error.value = completedTwo.error
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        is Result.Error -> {
                            _error.value = completedTwo.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        else -> {
                            _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                    }
                } else {

                    var completedOne = repository.getCompleted(_plans.value!![i].id, _groups.value!![0].membersID[1])
//                    Log.d("group","completedOne = $completedOne"+ "i = $i")
                    ownPlanCompleted.value = when (completedOne) {
                        is Result.Success -> {
                            _error.value = null
                            _status.value = LoadApiStatus.DONE
                            completedOne.data
                        }
                        is Result.Fail -> {
                            _error.value = completedOne.error
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        is Result.Error -> {
                            _error.value = completedOne.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        else -> {
                            _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                    }

                    var completedTwo = repository.getCompleted(_plans.value!![i].id, _groups.value!![0].membersID[0])
//                Log.d("group","completedTwo = $completedTwo"+ "i = $i")
                    partnerPlanCompleted.value = when (completedTwo) {
                        is Result.Success -> {
                            _error.value = null
                            _status.value = LoadApiStatus.DONE
                            completedTwo.data
                        }
                        is Result.Fail -> {
                            _error.value = completedTwo.error
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        is Result.Error -> {
                            _error.value = completedTwo.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                        else -> {
                            _error.value = PlanApplication.instance.getString(R.string.you_know_nothing)
                            _status.value = LoadApiStatus.ERROR
                            null
                        }
                    }
                }

                    when (_plans.value!![i].dueDate) {
                        -1L -> teamTarget(i)
                        else -> teamDueData(i)
                    }


                val filterPlan = _plans.value!!.filter {
                    it.taskDone == false
                }

                val filterPlanForShow = _plansForShow.value!!.filter {
                    it.taskDone == false
                }
//
                _plans.value = filterPlan

                _plansForShow.value = filterPlanForShow
            }
        }
    }

    fun setShowData(){
        var plan = mutableListOf<PlanForShow>()
        plan.run {
            for (i in plans.value!!.indices) {
                add(PlanForShow(
                        id = plans.value!![i].id,
                        name = plans.value!![i].name,
                        category = plans.value!![i].category,
                        createdTime = plans.value!![i].createdTime,
                        taskDone = plans.value!![i].taskDone,
                        members = plans.value!![i].members,
                        target = plans.value!![i].target,
                        dailyTarget = plans.value!![i].dailyTarget,
                        group = plans.value!![i].group,
                        dueDate = plans.value!![i].dueDate
                ))
            }
        }
        _plansForShow.value = plan
    }


    fun teamTarget(i: Int) {
        var sumOne : Int = 0
        for (j in ownPlanCompleted.value!!.indices){
            sumOne += ownPlanCompleted.value!![j].daily
            checkOwnTodayDone(j,i)
        }
        Log.d("HomeTeamFragment","sumOne = $sumOne")
        var sumtwo : Int = 0
        for (k in partnerPlanCompleted.value!!.indices){
            sumtwo += partnerPlanCompleted.value!![k].daily
            checkPartnerTodayDone(k,i)
        }
        Log.d("HomeTeamFragment","sumTwo = $sumtwo")
        var sum = sumOne + sumtwo

        _plansForShow.value!![i].progressTimeOwn = sumOne/60
        _plansForShow.value!![i].progressTimePartner = sumtwo/60
        _plansForShow.value!![i].progressTimeTotal = sum/60

        if (sum >= _plans.value!![i].target*60) {
            _plans.value!![i].taskDone = true
            _plansForShow.value!![i].taskDone = true
        }
    }

        fun teamDueData(i: Int) {

        var completedDayOne : Int = 0
        var completedDayTwo : Int = 0
        var totalCompletedDay: Long = (_plans.value!![i].dueDate - _plans.value!![i].createdTime)*2 / ONE_DAY_MILLI_SECOND

        for (j in ownPlanCompleted.value!!.indices){
            if (ownPlanCompleted.value!![j].completed){
                completedDayOne++
            }
            checkOwnTodayDone(j,i)
        }

        for (j in partnerPlanCompleted.value!!.indices){
            if (partnerPlanCompleted.value!![j].completed){
                completedDayTwo++
            }
            checkPartnerTodayDone(j,i)
        }

        _plansForShow.value!![i].progressTimeOwn = completedDayOne
        _plansForShow.value!![i].progressTimePartner = completedDayTwo
        _plansForShow.value!![i].progressTimeTotal = completedDayOne + completedDayTwo
        _plansForShow.value!![i].target = when(totalCompletedDay) {
            0L -> 1
            else -> totalCompletedDay.toInt()
        }

        val today = Calendar.getInstance().timeInMillis
        val dueDate = _plans.value!![i].dueDate

        if (dueDate <= today) {
            _plans.value!![i].taskDone = true
            _plansForShow.value!![i].taskDone = true
        }
    }


//    fun singleDueData(i: Int) {
//        var completedDay : Int = 0
//        var totalCompletedDay: Long = (_plans.value!![i].dueDate - _plans.value!![i].createdTime) / ONE_DAY_MILLI_SECOND
//        for (j in singlePlanCompleted.value!!.indices){
//            if (singlePlanCompleted.value!![j].completed){
//                completedDay++
//            }
//            checkTodayDone(j,i)
//        }
//        Log.d("test","totalCompletedDay = $totalCompletedDay")
//        Log.d("test","completedDay = $completedDay")
//
//        _plans.value!![i].progressTime = completedDay
//        _plans.value!![i].target = when(totalCompletedDay) {
//            0L -> 1
//            else -> totalCompletedDay.toInt()
//        }
//
//        val today = Calendar.getInstance().timeInMillis
//        val dueDate = _plans.value!![i].dueDate
//        Log.d("test","today = $today")
//        Log.d("test","dueDate = $dueDate")
//        if (dueDate <= today) {
//            _plans.value!![i].taskDone = true
//        }
//        Log.d("test","_plans.value!![i].taskDone = ${_plans.value!![i].taskDone}")
//
//    }


    fun checkOwnTodayDone(j: Int,i : Int) {

        val today = TimeConverters.timestampToDate(Calendar.getInstance().timeInMillis, Locale.TAIWAN)
        val completedDay = TimeConverters.timestampToDate(ownPlanCompleted.value!![j].date, Locale.TAIWAN)
        val completedUser = ownPlanCompleted.value!![j].user_id
//        Log.d("test1","completedUser = $completedUser")
//        Log.d("test1","FirebaseAuth.getInstance().currentUser!!.uid = ${FirebaseAuth.getInstance().currentUser!!.uid}")
        if (completedDay == today && completedUser == FirebaseAuth.getInstance().currentUser!!.uid) {
            _plans.value!![i].todayDone = true
            _plansForShow.value!![i].todayDone = true
        }
//        Log.d("test1","_plans.value!![i].todayDone = ${_plans.value!![i].todayDone}")
//        Log.d("test1","_plans.value!![i] = ${_plans.value!![i]}")

    }

    fun checkPartnerTodayDone(j: Int,i : Int) {

        val today = TimeConverters.timestampToDate(Calendar.getInstance().timeInMillis, Locale.TAIWAN)
        val completedDay = TimeConverters.timestampToDate(partnerPlanCompleted.value!![j].date, Locale.TAIWAN)
        val completedUser = partnerPlanCompleted.value!![j].user_id
//        Log.d("test1","completedUser = $completedUser")
//        Log.d("test1","FirebaseAuth.getInstance().currentUser!!.uid = ${FirebaseAuth.getInstance().currentUser!!.uid}")
        if (completedDay == today && completedUser == FirebaseAuth.getInstance().currentUser!!.uid) {
            _plans.value!![i].todayDone = true
            _plansForShow.value!![i].todayDone = true
        }
//        Log.d("test1","_plans.value!![i].todayDone = ${_plans.value!![i].todayDone}")
//        Log.d("test1","_plans.value!![i] = ${_plans.value!![i]}")

    }

    fun navigateTimer(plan: PlanForShow?) {
        _navigateToTimer.value = plan
    }


    fun deleteNavigateTimer() {
        _navigateToTimer.value = null
    }


}