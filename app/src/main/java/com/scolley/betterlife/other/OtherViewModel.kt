package com.scolley.betterlife.other

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scolley.betterlife.PlanApplication
import com.scolley.betterlife.R
import com.scolley.betterlife.data.Category
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

class OtherViewModel(private val repository: PlanRepository) : ViewModel() {

    private val _otherPlan = repository.getLiveOtherPlanResult()

    val otherPlan: LiveData<List<Plan>>
        get() = _otherPlan

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _category = MutableLiveData<Category>()

    val category: LiveData<Category>
        get() = _category

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

        _category.value = Category.NOFILTER

        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")

        getOtherPlanResult()
    }

    fun getCategoryTask(selected: Category) {
        _category.value = selected
    }

    fun getOtherSelectedPlanResult(category: Category) {

        coroutineScope.launch {

            if (category == Category.NOFILTER) {

                getOtherPlanResult()

            } else {

                _status.value = LoadApiStatus.LOADING

                val result = repository.getOtherSelectedPlanResult(category.category)
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
                _refreshStatus.value = false
                _otherPlan.value = _otherPlan.value
            }
        }
    }

    fun addToOtherTask(plan: Plan) {

        coroutineScope.launch {
            val result = repository.addToOtherTask(FirebaseAuth.getInstance().currentUser!!.uid, plan.id)
            _otherPlan.value = _otherPlan.value
        }

    }

    private fun getOtherPlanResult() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getOtherPlanResult()

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
            _refreshStatus.value = false
        }
    }
}