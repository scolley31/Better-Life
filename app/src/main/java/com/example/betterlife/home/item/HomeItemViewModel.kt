package com.example.betterlife.home.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betterlife.data.Completed
import com.example.betterlife.data.Plan
import com.example.betterlife.data.Source.PlanRepository
import com.example.betterlife.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeItemViewModel(private val repository: PlanRepository):ViewModel() {

    private val _plan = MutableLiveData<List<Plan>>()

    val plan: MutableLiveData<List<Plan>>
        get() = _plan

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

        setMockData()
    }


    fun setMockData() {

        var mock = mutableListOf<Plan>()
        mock.run {
            add(Plan("1", "跑步", "運動", "", "2200-0412",
                    listOf("Wayne","Sean"),"",3000,
                    listOf(Completed("1","Scolley",true,300,"2200-0412")
                            ,Completed("2","Wayne",true,300,"2200-0412")
                            ,Completed("3","Sean",true,300,"2200-0412"))))
            add(Plan("2", "跳繩", "運動", "", "2200-0412",
                    listOf("Wayne","Sean"),"",3000,
                    listOf(Completed("1","Scolley",true,300,"2200-0412")
                            ,Completed("2","Wayne",true,300,"2200-0412")
                            ,Completed("3","Sean",true,300,"2200-0412"))))
            add(Plan("3", "打籃球", "運動", "", "2200-0412",
                    listOf("Wayne","Sean"),"",3000,
                    listOf(Completed("1","Scolley",true,300,"2200-0412")
                            ,Completed("2","Wayne",true,300,"2200-0412")
                            ,Completed("3","Sean",true,300,"2200-0412"))))
            add(Plan("4", "打羽球", "運動", "", "2200-0412",
                    listOf("Wayne","Sean"),"",3000,
                    listOf(Completed("1","Scolley",true,300,"2200-0412")
                            ,Completed("2","Wayne",true,300,"2200-0412")
                            ,Completed("3","Sean",true,300,"2200-0412"))))
            add(Plan("5", "散步", "運動", "", "2200-0412",
                    listOf("Wayne","Sean"),"",3000,
                    listOf(Completed("1","Scolley",true,300,"2200-0412")
                            ,Completed("2","Wayne",true,300,"2200-0412")
                            ,Completed("3","Sean",true,300,"2200-0412"))))
        }
        _plan.value = mock
    }

}