package com.example.betterlife.home.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.PlanApplication
import com.example.betterlife.R
import com.example.betterlife.data.Plan
import com.example.betterlife.data.User
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.newwork.LoadApiStatus
import com.example.betterlife.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.betterlife.data.Result

class HomeItemViewModel(private val repository: PlanRepository):ViewModel() {

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

    private val _user = MutableLiveData<User>()

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

        mockUser()

//        if (PlanApplication.instance.isLiveDataDesign()) {
//            getLivePlanResult()
//        } else {
            getPlanResult()
//        }

//        setMockData()
    }

    private fun mockUser() {
        var mockUser = User()
        mockUser.run {
            this.userId = "scolley31"
            this.google_id = "scolley31"
            this.userImage = ""
            this.userName = "Scolley"
        }
        _user.value = mockUser
    }

    fun getLivePlanResult() {
        livePlans = repository.getLivePlanResult()
        _status.value = LoadApiStatus.DONE
        _refreshStatus.value = false
        Log.d("test","livePlans = ${livePlans.value}")
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
                }
            }
            Log.d("test","plan = ${_plans.value}")
            _refreshStatus.value = false
        }
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

//    fun setSumData() {
//        var progress : Float = 0F
//        for (i in plan.value!!.indices){
//            plan.value!![i].dailyTarget = (plan.value!![i].completedList.sumBy { it.daily })
//            progress = plan.value!![i].dailyTarget?.div(plan.value!![i].target)!!.toFloat()
//            plan.value!![i].progress = progress.toInt()
//            Log.i("test","After setSumData plan = ${plan.value}")
//        }
//
//    }

//    fun setMockData() {
//
//        var mock = mutableListOf<Plan>()
//        mock.run {
//            add(Plan("1", "跑步", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",4000,
//                    listOf(Completed("1","Scolley",true,300,"2200-0412")
//                            ,Completed("2","Wayne",true,300,"2200-0412")
//                            ,Completed("3","Sean",true,200,"2200-0412"))
//                            ,300
//                            ,null))
//
//            add(Plan("2", "跳繩", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",2000,
//                    listOf(Completed("1","Scolley",true,200,"2200-0412")
//                            ,Completed("2","Wayne",true,300,"2200-0412")
//                            ,Completed("3","Sean",true,100,"2200-0412"))
//                            ,300
//                            ,null))
//
//            add(Plan("3", "打籃球", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",10000,
//                    listOf(Completed("1","Scolley",true,100,"2200-0412")
//                            ,Completed("2","Wayne",true,500,"2200-0412")
//                            ,Completed("3","Sean",true,300,"2200-0412"))
//                            ,300
//                            ,null))
//
//            add(Plan("4", "打羽球", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",5000,
//                    listOf(Completed("1","Scolley",true,300,"2200-0412")
//                            ,Completed("2","Wayne",true,200,"2200-0412")
//                            ,Completed("3","Sean",true,300,"2200-0412"))
//                            ,300
//                            ,null))
//
//            add(Plan("5", "散步", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",3000,
//                    listOf(Completed("1","Scolley",true,300,"2200-0412")
//                            ,Completed("2","Wayne",true,300,"2200-0412")
//                            ,Completed("3","Sean",true,300,"2200-0412"))
//                            ,300
//                            ,null))
//        }
//        _plan.value = mock
//    }

}