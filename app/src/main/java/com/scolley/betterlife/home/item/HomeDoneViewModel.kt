package com.scolley.betterlife.home.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.R
import com.scolley.betterlife.data.*
import com.scolley.betterlife.data.source.PlanRepository
import com.scolley.betterlife.newwork.LoadApiStatus
import com.scolley.betterlife.util.Logger
import com.scolley.betterlife.util.TimeConverters
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class HomeDoneViewModel (private val repository: PlanRepository): ViewModel() {

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

    private val _navigateToAddTask = MutableLiveData<Boolean>()

    val navigateToAddTask: MutableLiveData<Boolean>
        get() = _navigateToAddTask

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

    val ownPlanCompleted = MutableLiveData<List<Completed>>()

    val partnerPlanCompleted = MutableLiveData<List<Completed>>()

    private val _plansForShow = MutableLiveData<List<PlanForShow>>()

    val plansForShow: LiveData<List<PlanForShow>>
        get() = _plansForShow

    private val _groups = MutableLiveData<List<Groups>>()

    val groups: LiveData<List<Groups>>
        get() = _groups

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
            Log.d("test","getPlanResult plan = ${_plans.value}")
            _refreshStatus.value = false

            getCompleted()


        }
    }

    fun getCompleted(){

        coroutineScope.launch {
            for (i in _plans.value!!.indices) {

                if (_plans.value!![i].group) {

                    var group = repository.getGroup(_plans.value!![i].id, user.value!!.userId)
                    Log.d("group","group = $group")
                    _groups.value = when (group) {
                        is Result.Success -> {
                            _error.value = null
//                            _status.value = LoadApiStatus.DONE
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
//                                _status.value = LoadApiStatus.DONE
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
//                                _status.value = LoadApiStatus.DONE
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
//                                _status.value = LoadApiStatus.DONE
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
//                                _status.value = LoadApiStatus.DONE
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

                } else {
                    var completed = repository.getCompleted(_plans.value!![i].id, user.value!!.userId)
                    singlePlanCompleted.value = when (completed) {
                        is Result.Success -> {
                            _error.value = null
//                            _status.value = LoadApiStatus.DONE
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
                        -1L -> singleTarget(i)
                        else -> singleDueData(i)
                    }
                    Log.d("test", "_plans.value!![i] = ${_plans.value!![i]}")
                }
            }

            val filter = _plans.value!!.filter {
                it.taskDone == true
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
        Log.d("test","totalCompletedDay = $totalCompletedDay")
        Log.d("test","completedDay = $completedDay")

        _plans.value!![i].progressTime = completedDay
        _plans.value!![i].target = when(totalCompletedDay) {
            0L -> 1
            else -> totalCompletedDay.toInt()
        }

        val today = Calendar.getInstance().timeInMillis
        val dueDate = _plans.value!![i].dueDate
//        Log.d("test","today = $today")
//        Log.d("test","dueDate = $dueDate")
        if (dueDate <= today) {
            _plans.value!![i].taskDone = true
        }
//        Log.d("test","_plans.value!![i].taskDone = ${_plans.value!![i].taskDone}")

    }

    fun teamTarget(i: Int) {
        var sumOne : Int = 0
        for (j in ownPlanCompleted.value!!.indices){
            sumOne += ownPlanCompleted.value!![j].daily
        }
        Log.d("HomeTeamFragment","sumOne = $sumOne")
        var sumtwo : Int = 0
        for (k in partnerPlanCompleted.value!!.indices){
            sumtwo += partnerPlanCompleted.value!![k].daily
        }
        Log.d("HomeTeamFragment","sumTwo = $sumtwo")
        var sum = sumOne + sumtwo

//        _plansForShow.value!![i].progressTimeOwn = sumOne/60
//        _plansForShow.value!![i].progressTimePartner = sumtwo/60
//        _plansForShow.value!![i].progressTimeTotal = sum/60
        _plans.value!![i].progressTime = sum/60

        if (sum >= _plans.value!![i].target*60) {
            _plans.value!![i].taskDone = true
//            _plansForShow.value!![i].taskDone = true
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
        }

        for (j in partnerPlanCompleted.value!!.indices){
            if (partnerPlanCompleted.value!![j].completed){
                completedDayTwo++
            }
        }

//        _plansForShow.value!![i].progressTimeOwn = completedDayOne
//        _plansForShow.value!![i].progressTimePartner = completedDayTwo
//        _plansForShow.value!![i].progressTimeTotal = completedDayOne + completedDayTwo
        _plans.value!![i].progressTime = completedDayOne + completedDayTwo

//        _plansForShow.value!![i].target = when(totalCompletedDay) {
//            0L -> 1
//            else -> totalCompletedDay.toInt()
//        }

        _plans.value!![i].target = when(totalCompletedDay) {
            0L -> 1
            else -> totalCompletedDay.toInt()
        }

        val today = Calendar.getInstance().timeInMillis
        val dueDate = _plans.value!![i].dueDate

        if (dueDate <= today) {
            _plans.value!![i].taskDone = true
//            _plansForShow.value!![i].taskDone = true
        }
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

    fun navigateAddTask () {
        _navigateToAddTask.value = true
    }


}