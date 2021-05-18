package com.example.betterlife.other

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.data.Completed
import com.example.betterlife.data.Plan
import com.example.betterlife.data.source.PlanRepository
import com.example.betterlife.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class OtherViewModel(private val repository: PlanRepository) : ViewModel()  {

    private val _otherPlan = MutableLiveData<List<Plan>>()

    val otherPlan: MutableLiveData<List<Plan>>
        get() = _otherPlan

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

//        setMockData()
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