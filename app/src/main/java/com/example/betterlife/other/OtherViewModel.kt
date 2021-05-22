package com.example.betterlife.other

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

class OtherViewModel(private val repository: PlanRepository) : ViewModel()  {

    private val _otherPlan = repository.getLiveOtherPlanResult()

    val otherPlan: LiveData<List<Plan>>
        get() = _otherPlan

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

        getOtherPlanResult()

//        setMockData()
    }

    fun addToOtherTask(plan: Plan) {

        coroutineScope.launch {

                val result = repository.addToOtherTask("Scolley",plan.id)
                Log.d("result","result = $result")
                getOtherPlanResult()
            
        }

    }


    fun getOtherPlanResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getOtherPlanResult()

            Log.d("test","result = $result")

            _otherPlan.value = when (result) {
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
            Log.d("test","_otherPlan = ${_otherPlan.value}")
            _refreshStatus.value = false
        }
    }


//    fun setMockData() {
//
//        var mock = mutableListOf<Plan>()
//        mock.run {
//            add(Plan("1", "跑步", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",4000,
//                    listOf(Completed("1","Scolley",true,300,"2200-0412")
//                            , Completed("2","Wayne",true,300,"2200-0412")
//                            , Completed("3","Sean",true,200,"2200-0412"))
//                    ,300
//                    ,null))
//
//            add(Plan("2", "跳繩", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",2000,
//                    listOf(Completed("1","Scolley",true,200,"2200-0412")
//                            , Completed("2","Wayne",true,300,"2200-0412")
//                            , Completed("3","Sean",true,100,"2200-0412"))
//                    ,300
//                    ,null))
//
//            add(Plan("3", "打籃球", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",10000,
//                    listOf(Completed("1","Scolley",true,100,"2200-0412")
//                            , Completed("2","Wayne",true,500,"2200-0412")
//                            , Completed("3","Sean",true,300,"2200-0412"))
//                    ,300
//                    ,null))
//
//            add(Plan("4", "打羽球", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",5000,
//                    listOf(Completed("1","Scolley",true,300,"2200-0412")
//                            , Completed("2","Wayne",true,200,"2200-0412")
//                            , Completed("3","Sean",true,300,"2200-0412"))
//                    ,300
//                    ,null))
//
//            add(Plan("5", "散步", "運動", "", "2200-0412",
//                    listOf("Wayne","Sean"),"",3000,
//                    listOf(Completed("1","Scolley",true,300,"2200-0412")
//                            , Completed("2","Wayne",true,300,"2200-0412")
//                            , Completed("3","Sean",true,300,"2200-0412"))
//                    ,300
//                    ,null))
//        }
//        _otherPlan.value = mock
//    }
}